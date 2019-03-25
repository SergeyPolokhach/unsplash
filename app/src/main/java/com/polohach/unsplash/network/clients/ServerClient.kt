package com.polohach.unsplash.network.clients

import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.polohach.unsplash.BuildConfig
import com.polohach.unsplash.network.NetworkModule.mapper
import com.polohach.unsplash.network.api.modules.UnsplashModule
import com.polohach.unsplash.network.api.modules.UnsplashModuleImpl
import com.polohach.unsplash.network.api.retrofit.UnsplashApi
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

class ServerClient {

    companion object {
        private const val TIMEOUT_IN_SECONDS = 30L
        private const val REQUEST_TAG = "Request>>>>"
        private const val RESPONSE_TAG = "Response<<<<"
    }

    val unsplash: UnsplashModule by lazy { UnsplashModuleImpl(retrofit.create(UnsplashApi::class.java)) }

    private val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .baseUrl(BuildConfig.ENDPOINT)
            .client(createHttpClient())
            .build()

    private fun log() = LoggingInterceptor.Builder()
            .loggable(BuildConfig.DEBUG)
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .request(REQUEST_TAG)
            .response(RESPONSE_TAG)
            .build()

    private fun createHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                    requestBuilder.method(original.method(), original.body())
                    return@addInterceptor chain.proceed(requestBuilder.build())
                }
        if (BuildConfig.DEBUG) addInterceptor(log())
    }.build()
}
