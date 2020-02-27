package com.polohach.unsplash.di

import com.polohach.unsplash.network.api.modules.UnsplashModule
import com.polohach.unsplash.providers.UnsplashProvider
import com.polohach.unsplash.providers.UnsplashProviderImpl
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun providerUnsplashProvider(unsplashModule: UnsplashModule): UnsplashProvider =
            UnsplashProviderImpl(unsplashModule)
}