package com.polohach.unsplash.ui.screens.main.photo.detail

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_core.utils.ImageUtils
import com.cleveroad.bootstrap.kotlin_ext.safeLet
import com.polohach.unsplash.App
import com.polohach.unsplash.providers.UnsplashProvider
import com.polohach.unsplash.ui.base.BaseViewModel
import com.polohach.unsplash.utils.OptionalWrapper
import com.polohach.unsplash.utils.wrapOptional
import io.reactivex.Single
import java.io.File
import javax.inject.Inject

class DetailPhotoVM(application: Application) : BaseViewModel(application) {

    init {
        App.appComponent.inject(this)
    }

    @Inject
    lateinit var unsplash: UnsplashProvider

    val sharePhotoLD = MutableLiveData<OptionalWrapper<File>>()

    private val downloadPhotoLD = MutableLiveData<OptionalWrapper<File>>()

    fun downloadPhoto(context: Context?, id: String) {
        unsplash.downloadPhoto(id)
                .flatMap { createFileTemp(context, it.value) }
                .doAsync(downloadPhotoLD)
    }

    fun createShareFile(context: Context?, bitmap: Bitmap?) {
        createFileTemp(context, bitmap)
                .doAsync(sharePhotoLD)
    }

    private fun createFileTemp(context: Context?, bitmap: Bitmap?): Single<OptionalWrapper<File>> =
            Single.fromCallable {
                var file: File? = null
                safeLet(context, bitmap) { ctx, photo ->
                    ImageUtils.createImageFileTemp(ctx, true).also { fileTemp ->
                        ImageUtils.saveBitmap(fileTemp, Bitmap.createBitmap(photo))
                        ContentValues().apply {
                            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                            put(MediaStore.MediaColumns.DATA, fileTemp.absolutePath)
                            ctx.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, this)
                        }
                        file = fileTemp
                    }
                }
                file.wrapOptional()
            }
}
