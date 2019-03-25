package com.polohach.unsplash

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex


class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
