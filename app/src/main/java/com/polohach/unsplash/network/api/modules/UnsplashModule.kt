package com.polohach.unsplash.network.api.modules

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.polohach.unsplash.models.Photo
import com.polohach.unsplash.network.api.beans.PhotoBean
import com.polohach.unsplash.network.api.converters.PhotoBeanConverterImpl
import com.polohach.unsplash.network.api.retrofit.UnsplashApi
import com.polohach.unsplash.utils.EMPTY_STRING
import com.polohach.unsplash.utils.OptionalWrapper
import com.polohach.unsplash.utils.printLogE
import com.polohach.unsplash.utils.wrapOptional
import io.reactivex.Single
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

interface UnsplashModule {

    fun getPhotos(page: Int): Single<List<Photo>>

    fun searchPhotos(query: String, page: Int): Single<List<Photo>>

    fun downloadPhoto(id: String): Single<OptionalWrapper<Bitmap>>
}

class UnsplashModuleImpl(api: UnsplashApi) :
        BaseRxModule<UnsplashApi, PhotoBean, Photo>(api, PhotoBeanConverterImpl()), UnsplashModule {

    override fun getPhotos(page: Int): Single<List<Photo>> =
            api.getPhotos(page)
                    .onErrorResumeNext(NetworkErrorUtils.rxParseSingleError())
                    .map { converter.convertListInToOut(it.body()) }

    override fun searchPhotos(query: String, page: Int): Single<List<Photo>> =
            api.searchPhotos(query, page)
                    .onErrorResumeNext(NetworkErrorUtils.rxParseSingleError())
                    .map { converter.convertListInToOut(it.body()?.results) }

    override fun downloadPhoto(id: String): Single<OptionalWrapper<Bitmap>> =
            api.downloadPhoto(id)
                    .onErrorResumeNext(NetworkErrorUtils.rxParseSingleError())
                    .map { it.body()?.url ?: EMPTY_STRING }
                    .map { getBitmapFromURL(it) }

    private fun getBitmapFromURL(src: String): OptionalWrapper<Bitmap> {
        var result: Bitmap? = null
        var connection: HttpURLConnection? = null
        try {
            connection = (URL(src).openConnection() as HttpURLConnection).apply {
                doInput = true
                connect()
                result = BitmapFactory.decodeStream(
                        inputStream,
                        null,
                        BitmapFactory.Options()
                                .apply { inPreferredConfig = Bitmap.Config.RGB_565 })
            }
        } catch (e: IOException) {
            e.printLogE()
        } finally {
            connection?.disconnect()
        }
        return result.wrapOptional()
    }
}
