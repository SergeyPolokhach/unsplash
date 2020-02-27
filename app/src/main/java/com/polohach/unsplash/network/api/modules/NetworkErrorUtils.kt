package com.polohach.unsplash.network.api.modules

import com.cleveroad.bootstrap.kotlin_core.network.ApiException
import com.cleveroad.bootstrap.kotlin_core.network.ValidationError
import com.fasterxml.jackson.databind.ObjectMapper
import com.polohach.unsplash.network.api.errors.ServerError
import com.polohach.unsplash.network.exceptions.NoNetworkException
import com.polohach.unsplash.network.exceptions.ServerException
import com.polohach.unsplash.utils.printLogE
import io.reactivex.Single
import io.reactivex.functions.Function
import retrofit2.HttpException
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.net.ConnectException
import java.net.HttpURLConnection.HTTP_BAD_GATEWAY
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

class NetworkErrorUtils(private val mapper: ObjectMapper) {

    fun <T> rxParseSingleError() = Function<Throwable, Single<T>> {
        Single.error<T>(parseError(it))
    }

    private fun parseError(throwable: Throwable): Throwable? {
        return if (throwable is HttpException) {
            val code = throwable.code()
            if (code == HTTP_INTERNAL_ERROR || code == HTTP_BAD_GATEWAY) {
                throwable.message().printLogE()
                return ServerException().initCause(throwable)
            }
            return parseErrorResponseBody(throwable.response())
        } else when {
            isConnectionProblem(throwable) -> NoNetworkException()
            isServerConnectionProblem(throwable) -> ServerException()
            else -> throwable
        }
    }

    private fun isServerConnectionProblem(throwable: Throwable) =
            throwable is SocketException || throwable is SocketTimeoutException

    private fun isConnectionProblem(throwable: Throwable) =
            throwable is UnknownHostException || throwable is ConnectException

    private fun parseErrorResponseBody(response: Response<*>): Exception {
        var inputStreamReader: InputStreamReader? = null
        var bufferedReader: BufferedReader? = null
        try {
            inputStreamReader = InputStreamReader(response.errorBody()?.byteStream())
            bufferedReader = BufferedReader(inputStreamReader)
            val sb = StringBuilder()
            var newLine: String? = null
            while ({ newLine = bufferedReader.readLine(); newLine }() != null) {
                sb.append(newLine)
            }

            // Try to parse ServerError.class
            val serverError: ServerError
            try {
                serverError = mapper.readValue(sb.toString(), ServerError::class.java)
            } catch (e: IOException) {
                "Couldn't parse error response to ServerError.class: ${e.message}".printLogE()
                return e
            }

            val validationErrors = ArrayList<ValidationError>()
            serverError.errors?.forEach {
                validationErrors.add(ValidationError(it.code, it.key, it.message))
            }

            return ApiException(response.code(),
                    serverError.v,
                    serverError.message,
                    validationErrors)

        } catch (e: IOException) {
            e.message.printLogE()
            return e
        } finally {
            closeReader(bufferedReader)
            closeReader(inputStreamReader)
        }
    }

    private fun closeReader(reader: Reader?) {
        reader?.let {
            try {
                it.close()
            } catch (e: IOException) {
                e.message.printLogE()
            }
        }
    }
}
