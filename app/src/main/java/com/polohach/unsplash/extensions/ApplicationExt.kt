package com.polohach.unsplash.extensions

import androidx.annotation.StringRes
import com.polohach.unsplash.App

fun getStringApp(@StringRes resId: Int): String = App.instance.getString(resId)
