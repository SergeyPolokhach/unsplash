package com.polohach.unsplash.providers

object ProviderInjector {
    val unsplash: UnsplashProvider by lazy { UnsplashProviderImpl() }
}
