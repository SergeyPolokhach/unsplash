package com.polohach.unsplash.di

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.polohach.unsplash.BuildConfig
import com.polohach.unsplash.network.REQUEST_TAG
import com.polohach.unsplash.network.RESPONSE_TAG
import com.polohach.unsplash.network.TIMEOUT_IN_SECONDS
import com.polohach.unsplash.network.api.retrofit.UnsplashApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun providerUnsplashService(retrofit: Retrofit): UnsplashApi =
            retrofit.create(UnsplashApi::class.java)

    @Provides
    @Singleton
    fun providerRetrofit(jacksonConverterFactory: JacksonConverterFactory,
                         rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
                         okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .addCallAdapterFactory(rxJava2CallAdapterFactory)
                    .addConverterFactory(jacksonConverterFactory)
                    .baseUrl(BuildConfig.ENDPOINT)
                    .client(okHttpClient)
                    .build()

    @Provides
    @Singleton
    fun providerOkHttpClient(interceptor: Interceptor): OkHttpClient =
            OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val requestBuilder = original.newBuilder()
                        requestBuilder.method(original.method(), original.body())
                        return@addInterceptor chain.proceed(requestBuilder.build())
                    }
                    .apply { if (BuildConfig.DEBUG) addInterceptor(interceptor) }
                    .build()

    @Provides
    @Singleton
    fun providerLoggingInterceptor(): Interceptor = LoggingInterceptor.Builder()
            .loggable(BuildConfig.DEBUG)
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .request(REQUEST_TAG)
            .response(RESPONSE_TAG)
            .build()

    @Provides
    @Singleton
    fun providerAdapterFactory(): RxJava2CallAdapterFactory =
            RxJava2CallAdapterFactory.create()

    @Provides
    @Singleton
    fun providerJacksonConverterFactory(mapper: ObjectMapper): JacksonConverterFactory =
            JacksonConverterFactory.create(mapper)

    @Provides
    @Singleton
    fun providerObjectMapper(jodaModule: JodaModule): ObjectMapper =
            ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .registerModule(jodaModule)

    @Provides
    @Singleton
    fun providerJodaModule(): JodaModule = JodaModule()
}