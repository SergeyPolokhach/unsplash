package com.polohach.unsplash.providers

import com.polohach.unsplash.models.Photo
import com.polohach.unsplash.network.NetworkModule
import com.polohach.unsplash.network.api.modules.UnsplashModule
import com.polohach.unsplash.providers.base.BaseOnlineProvider
import io.reactivex.Single

interface UnsplashProvider {

    fun getPhotos(page: Int): Single<List<Photo>>

    fun searchPhotos(query: String, page: Int): Single<List<Photo>>

    fun downloadPhoto(id: String): Single<String>
}

internal class UnsplashProviderImpl : BaseOnlineProvider<Photo, UnsplashModule>(), UnsplashProvider {

    override fun initNetworkModule() = NetworkModule.client.unsplash

    override fun getPhotos(page: Int): Single<List<Photo>> =
            networkModule.getPhotos(page)

    override fun searchPhotos(query: String, page: Int): Single<List<Photo>> =
            networkModule.searchPhotos(query, page)

    override fun downloadPhoto(id: String): Single<String> =
            networkModule.downloadPhoto(id)
}
