package com.polohach.unsplash.network.api.retrofit

import com.polohach.unsplash.network.api.beans.DownloadBean
import com.polohach.unsplash.network.api.beans.PhotoBean
import com.polohach.unsplash.network.api.beans.SearchPhotoBean
import com.polohach.unsplash.network.auth
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface UnsplashApi {

    @GET("/photos/?$auth")
    fun getPhotos(@Query("page") page: Int): Single<Response<List<PhotoBean>>>

    @GET("/search/photos/?$auth")
    fun searchPhotos(@Query("query") query: String,
                     @Query("page") page: Int): Single<Response<SearchPhotoBean>>

    @GET("/photos/{id}/download/?$auth")
    fun downloadPhoto(@Path("id") id: String): Single<Response<DownloadBean>>
}
