package com.polohach.unsplash.ui.screens.splash

import android.os.Bundle
import com.polohach.unsplash.R
import com.polohach.unsplash.ui.base.BaseActivity
import com.polohach.unsplash.ui.screens.main.MainActivity

class SplashActivity : BaseActivity<SplashViewModel>() {

    override val containerId = R.id.container
    override val layoutId = R.layout.activity_splash
    override val viewModelClass = SplashViewModel::class.java

    override fun hasProgressBar() = false

    override fun onCreate(savedInstanceState: Bundle?) =
            super.onCreate(savedInstanceState).also { openAuthScreen() }

    override fun observeLiveData(viewModel: SplashViewModel) = Unit

    private fun openAuthScreen() {
        MainActivity.start(this)
        finish()
    }
}
