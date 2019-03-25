package com.polohach.unsplash.ui.screens.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.polohach.unsplash.R
import com.polohach.unsplash.ui.base.BaseActivity
import com.polohach.unsplash.ui.screens.main.photo.detail.DetailPhotoFragment
import com.polohach.unsplash.ui.screens.main.photo.list.ListPhotosCallback
import com.polohach.unsplash.ui.screens.main.photo.list.ListPhotosFragment

class MainActivity : BaseActivity<MainViewModel>(), ListPhotosCallback {

    companion object {
        fun start(context: Context?) {
            context?.apply ctx@{ startActivity(getIntent(this@ctx)) }
        }

        fun getIntent(context: Context?) = Intent(context, MainActivity::class.java)
    }

    override val containerId = R.id.container
    override val layoutId = R.layout.activity_main
    override val viewModelClass = MainViewModel::class.java

    override fun hasProgressBar() = true

    override fun onCreate(savedInstanceState: Bundle?) =
            super.onCreate(savedInstanceState).also {
                if (savedInstanceState == null) openListPhotos()
            }

    override fun observeLiveData(viewModel: MainViewModel) = Unit

    override fun openDetailPhoto(id: String, downloadUrl: String, photoUrl: String) {
        replaceFragment(DetailPhotoFragment.newInstance(id, downloadUrl, photoUrl))
    }

    private fun openListPhotos() {
        replaceFragment(ListPhotosFragment.newInstance(), false)
    }
}
