package com.polohach.unsplash.providers.base

import com.polohach.unsplash.models.Model


abstract class BaseOnlineProvider<M : Model, NetworkModule> : Provider<M> {

    val networkModule: NetworkModule = this.initNetworkModule()

    protected abstract fun initNetworkModule(): NetworkModule
}
