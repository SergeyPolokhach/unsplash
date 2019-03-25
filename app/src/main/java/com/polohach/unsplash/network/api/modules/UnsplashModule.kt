package com.polohach.unsplash.network.api.modules

import com.polohach.unsplash.models.Photo
import com.polohach.unsplash.network.api.beans.PhotoBean
import com.polohach.unsplash.network.api.converters.PhotoBeanConverterImpl
import com.polohach.unsplash.network.api.retrofit.UnsplashApi
import com.polohach.unsplash.utils.EMPTY_STRING
import io.reactivex.Single

interface UnsplashModule {

    fun getPhotos(page: Int): Single<List<Photo>>

    fun searchPhotos(query: String, page: Int): Single<List<Photo>>

    fun downloadPhoto(id: String): Single<String>
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

    override fun downloadPhoto(id: String): Single<String> =
            api.downloadPhoto(id)
                    .onErrorResumeNext(NetworkErrorUtils.rxParseSingleError())
                    .map { it.body()?.url ?: EMPTY_STRING }
}
