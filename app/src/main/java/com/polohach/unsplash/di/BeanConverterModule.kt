package com.polohach.unsplash.di

import com.polohach.unsplash.network.api.converters.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BeanConverterModule {

    @Provides
    @Singleton
    fun providerLinksBeanConverter(): LinksBeanConverter =
            LinksBeanConverterImpl()

    @Provides
    @Singleton
    fun providerUrlsBeanConverter(): UrlsBeanConverter =
            UrlsBeanConverterImpl()

    @Provides
    @Singleton
    fun providerPhotoBeanConverter(linksBeanConverter: LinksBeanConverter,
                                   urlsBeanConverter: UrlsBeanConverter): PhotoBeanConverter =
            PhotoBeanConverterImpl(linksBeanConverter, urlsBeanConverter)
}