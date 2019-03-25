package com.polohach.unsplash.ui.screens.main.photo.list

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.polohach.unsplash.models.Photo
import com.polohach.unsplash.providers.ProviderInjector
import com.polohach.unsplash.ui.base.BaseViewModel
import com.polohach.unsplash.utils.EMPTY_STRING
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import java.util.concurrent.TimeUnit

class ListPhotosVM(application: Application) : BaseViewModel(application) {

    companion object {
        private const val FIRST_PAGE = 1
        private const val NETWORK_DELAY = 1000L
        private const val MIN_QUERY_LENGTH = 3
    }

    val currentStateLD = MutableLiveData<Parcelable?>()
    val initialPhotosLD = MutableLiveData<List<Photo>>()
    val morePhotosLD = MutableLiveData<List<Photo>>()

    var searchQuery = EMPTY_STRING

    private val publishProcessor = PublishProcessor.create<String>()

    fun subscribePublishProcessor() {
        publishProcessor
                .debounce(NETWORK_DELAY, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.isNotBlank() && it.length >= MIN_QUERY_LENGTH }
                .map { searchPhotos(initialPhotosLD, it, FIRST_PAGE) }
                .subscribe()
                .addSubscription()
    }

    fun loadInitial() {
        loadData(initialPhotosLD, FIRST_PAGE)
    }

    fun loadMoreData(page: Int) {
        loadData(morePhotosLD, page + FIRST_PAGE)
    }

    fun searchPhotos(query: String) {
        searchQuery = query
        publishProcessor.onNext(query)
    }

    private fun loadData(liveData: MutableLiveData<List<Photo>>, page: Int) {
        searchQuery.run {
            if (isNotBlank() && length >= MIN_QUERY_LENGTH) {
                searchPhotos(liveData, this, page)
            } else {
                loadPhotos(liveData, page)
            }
        }
    }

    private fun loadPhotos(liveData: MutableLiveData<List<Photo>>, page: Int) {
        ProviderInjector.unsplash.getPhotos(page)
                .doAsync(liveData, isShowProgress = false)
    }

    private fun searchPhotos(liveData: MutableLiveData<List<Photo>>, query: String, page: Int) {
        ProviderInjector.unsplash.searchPhotos(query, page)
                .doAsync(liveData, isShowProgress = false)
    }
}
