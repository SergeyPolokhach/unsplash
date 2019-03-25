package com.polohach.unsplash.ui.base

import android.view.View.NO_ID
import androidx.lifecycle.Observer
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleFragment
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleViewModel
import com.polohach.unsplash.BuildConfig
import com.polohach.unsplash.R

abstract class BaseFragment<T : BaseLifecycleViewModel> : BaseLifecycleFragment<T>() {

    override var endpoint = BuildConfig.ENDPOINT

    override var versionName = BuildConfig.VERSION_NAME

    override fun getVersionsLayoutId() = NO_ID

    override fun getEndPointTextViewId() = NO_ID

    override fun getVersionsTextViewId() = NO_ID

    override fun isDebug() = BuildConfig.DEBUG

    override val progressObserver = Observer<Boolean> {
        if (it == true) showProgress() else hideProgress()
    }

    // override this method if you need to show a warning when going to action back
    override fun showBlockBackAlert() = Unit

    override fun onStop() {
        hideProgress()
        super.onStop()
    }

    override fun onError(error: Any) {
        when (error) {
            is Throwable -> showSnackBar(error.message)
            is String -> showSnackBar(error)
            else -> showSnackBar(R.string.something_went_wrong)
        }
    }
}
