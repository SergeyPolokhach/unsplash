package com.polohach.unsplash.utils

import com.cleveroad.bootstrap.kotlin_core.ui.NotImplementedInterfaceException


inline fun <reified T> bindInterfaceOrThrow(vararg objects: Any?): T = objects.find { it is T }
        ?.let { it as T }
        ?: throw NotImplementedInterfaceException(T::class.java)

class OptionalWrapper<out T>(val value: T?)

fun <T> T?.wrapOptional() = OptionalWrapper(this)
