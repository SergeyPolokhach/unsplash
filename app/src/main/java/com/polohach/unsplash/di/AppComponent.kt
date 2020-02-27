package com.polohach.unsplash.di

import com.polohach.unsplash.ui.screens.main.photo.detail.DetailPhotoVM
import com.polohach.unsplash.ui.screens.main.photo.list.ListPhotosVM
import dagger.Component
import javax.inject.Singleton

@Component(modules = [RetrofitModule::class, BeanConverterModule::class,
    NetworkServiceModule::class, DataModule::class])
@Singleton
interface AppComponent {

    fun inject(viewModel: DetailPhotoVM)

    fun inject(viewModel: ListPhotosVM)
}