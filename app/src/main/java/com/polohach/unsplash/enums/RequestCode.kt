package com.polohach.unsplash.enums

private var requestCode = 1

enum class RequestCode {

    REQUEST_CREATE_TEMP_FILE;

    private val value = requestCode++

    operator fun invoke() = value
}
