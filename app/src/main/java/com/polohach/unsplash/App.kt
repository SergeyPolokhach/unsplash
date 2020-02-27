package com.polohach.unsplash

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.polohach.unsplash.di.AppComponent
import com.polohach.unsplash.di.DaggerAppComponent

class App : Application() {

    companion object {
        lateinit var instance: App
            private set

        lateinit var appComponent: AppComponent
            private set
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeDagger()
    }

    private fun initializeDagger() {
        appComponent = DaggerAppComponent.create()
    }
}
