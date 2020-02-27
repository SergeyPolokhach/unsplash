package com.polohach.unsplash.di

import com.polohach.unsplash.network.api.converters.PhotoBeanConverter
import com.polohach.unsplash.network.api.modules.NetworkErrorUtils
import com.polohach.unsplash.network.api.modules.UnsplashModule
import com.polohach.unsplash.network.api.modules.UnsplashModuleImpl
import com.polohach.unsplash.network.api.retrofit.UnsplashApi
import dagger.Module
import dagger.Provides

@Module
class NetworkServiceModule {

    @Provides
    fun providerUnsplashService(api: UnsplashApi,
                               errorUtils: NetworkErrorUtils,
                               photoBeanConverter: PhotoBeanConverter): UnsplashModule =
            UnsplashModuleImpl(api, errorUtils, photoBeanConverter)
}