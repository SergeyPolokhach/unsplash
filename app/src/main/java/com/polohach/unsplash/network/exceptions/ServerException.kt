package com.polohach.unsplash.network.exceptions

import com.cleveroad.bootstrap.kotlin_core.network.ApiException
import com.polohach.unsplash.R
import com.polohach.unsplash.extensions.getStringApp
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR


class ServerException : ApiException() {

    companion object {
        private val ERROR_MESSAGE = getStringApp(R.string.server_error)
    }

    override val message: String = ERROR_MESSAGE
    override var statusCode: Int? = HTTP_INTERNAL_ERROR
}
