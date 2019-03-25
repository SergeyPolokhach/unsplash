package com.polohach.unsplash.ui.screens.main.photo.detail

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.polohach.unsplash.App
import com.polohach.unsplash.providers.ProviderInjector
import com.polohach.unsplash.ui.base.BaseViewModel

class DetailPhotoVM(application: Application) : BaseViewModel(application) {

    val downloadPhotoLD = MutableLiveData<Bitmap>()

    fun downloadPhoto(id: String) {
        ProviderInjector.unsplash.downloadPhoto(id)
                .map { uri ->
                    Glide.with(App.instance)
                            .asBitmap()
                            .load(uri)
                            .submit()
                            .get()
                }
                .doAsync(downloadPhotoLD)
    }
}
