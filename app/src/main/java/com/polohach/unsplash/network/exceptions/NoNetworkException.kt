package com.polohach.unsplash.network.exceptions

import com.polohach.unsplash.R
import com.polohach.unsplash.extensions.getStringApp

class NoNetworkException : Exception() {

    companion object {
        private val ERROR_MESSAGE = getStringApp(R.string.no_internet_connection_error)
    }

    override val message: String = ERROR_MESSAGE
}
