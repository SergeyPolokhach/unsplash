package com.polohach.unsplash.extensions

import android.view.View
import androidx.annotation.IdRes


inline fun <reified T : View> View.find(@IdRes id: Int): T = findViewById(id)

fun View.showOrHide() = this.apply {
    visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
}
