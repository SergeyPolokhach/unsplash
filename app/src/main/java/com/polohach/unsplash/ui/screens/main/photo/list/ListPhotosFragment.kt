package com.polohach.unsplash.ui.screens.main.photo.list

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.bootstrap.kotlin_ext.safeLet
import com.polohach.unsplash.R
import com.polohach.unsplash.models.Photo
import com.polohach.unsplash.ui.PAGE_LIMIT
import com.polohach.unsplash.ui.VISIBLE_THRESHOLD
import com.polohach.unsplash.ui.base.BaseListFragment
import com.polohach.unsplash.ui.screens.main.photo.list.adapter.PhotoAdapter
import com.polohach.unsplash.utils.bindInterfaceOrThrow
import kotlinx.android.synthetic.main.fragment_list_photos.*


class ListPhotosFragment : BaseListFragment<ListPhotosVM, Photo>(), OnPhotoClickListener {

    companion object {
        private const val TWO_SPAN_COUNT = 2
        private const val THREE_SPAN_COUNT = 3

        fun newInstance() = ListPhotosFragment().apply {
            arguments = Bundle()
        }
    }

    override val layoutId = R.layout.fragment_list_photos
    override val viewModelClass = ListPhotosVM::class.java
    override val noResultViewId = R.id.tvEmptyPhotos
    override val recyclerViewId = R.id.rvPhotos
    override val refreshLayoutId = R.id.srPhotos
    override val viewModel: ListPhotosVM by lazy(LazyThreadSafetyMode.NONE) {
        activity
                ?.let { ViewModelProviders.of(it).get(viewModelClass) }
                ?: let { ViewModelProviders.of(this).get(viewModelClass) }
    }
    override var visibleThreshold = VISIBLE_THRESHOLD

    private var callback: ListPhotosCallback? = null
    private var adapter: PhotoAdapter? = null

    override fun getScreenTitle() = R.string.title_list_photos

    override fun getToolbarId() = R.id.toolbar

    override fun needToShowBackNav() = false

    override fun hasToolbar() = true

    override fun getAdapter() = adapter
            ?: context?.let { PhotoAdapter(it, this@ListPhotosFragment) }
                    .apply { adapter = this }

    override fun getLayoutManager(): RecyclerView.LayoutManager =
            GridLayoutManager(context, calculateSpanCount(), RecyclerView.VERTICAL, false)

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<ListPhotosCallback>(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) =
            super.onCreate(savedInstanceState).also { setHasOptionsMenu(true) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) =
            super.onViewCreated(view, savedInstanceState).also { setupUi() }

    override fun observeLiveData(viewModel: ListPhotosVM) {
        viewModel.apply {
            subscribePublishProcessor()
            initialPhotosLD.observe(this@ListPhotosFragment, Observer {
                onInitialDataLoaded(it)
            })
            morePhotosLD.observe(this@ListPhotosFragment, Observer {
                hideLoadingProgress()
                onDataRangeLoaded(it)
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_photos_search, menu)
        onSearch(menu.findItem(R.id.itSearch))
    }

    override fun loadInitial() {
        showLoadingProgress()
        viewModel.loadInitial()
    }

    override fun loadMoreData() {
        adapter?.run {
            itemCount.takeIf { it > 0 }?.apply {
                showLoadingProgress()
                viewModel.loadMoreData(this / PAGE_LIMIT)
            }
        }
    }

    override fun itemClick(photo: Photo) {
        photo.run {
            safeLet(id, links?.downloadLocation, urls?.regular) { id, downloadUrl, photoUrl ->
                callback?.openDetailPhoto(id, downloadUrl, photoUrl)
            }
        }
    }

    override fun onStop() {
        saveState()
        super.onStop()
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }

    private fun setupUi() {
        viewModel.apply {
            safeLet(initialPhotosLD.value, currentStateLD.value) { data, state ->
                if (data.isNotEmpty()) onInitialDataLoaded(data)
                (rvPhotos.layoutManager as? GridLayoutManager)?.apply {
                    onRestoreInstanceState(state)
                    spanCount = calculateSpanCount()
                    requestLayout()
                }
            } ?: loadInitial()
        }
    }

    private fun saveState() {
        viewModel.apply {
            initialPhotosLD.value = adapter?.snapshot
            (rvPhotos.layoutManager as? GridLayoutManager)?.run {
                currentStateLD.value = onSaveInstanceState()
            }
        }
    }

    private fun onSearch(item: MenuItem) {
        (item.actionView as? SearchView)?.apply {
            viewModel.searchQuery.takeIf { it.isNotBlank() }?.let {
                item.expandActionView()
                setQuery(it, true)
                clearFocus()
            }
            setOnQueryTextListener(
                    object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(s: String) = false

                        override fun onQueryTextChange(s: String) =
                                false.also { searchPhotos(s) }
                    })
        }
    }

    private fun searchPhotos(query: String) {
        viewModel.searchPhotos(query)
    }

    private fun calculateSpanCount() =
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> THREE_SPAN_COUNT
                else -> TWO_SPAN_COUNT
            }
}
