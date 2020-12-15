package com.gksoftwaresolutions.comicviewer.data

import com.gksoftwaresolutions.comicviewer.util.Common.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    private val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    var retrofit: Retrofit? = null

    private val logging = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val httpClientBuilder = OkHttpClient.Builder()

    fun <S> createService(serviceClass: Class<S>): S {
        if (!httpClientBuilder.interceptors().contains(logging)) {
            httpClientBuilder.addInterceptor(logging)
            retrofitBuilder.client(httpClientBuilder.build())
            retrofit = retrofitBuilder.build()
        }
        return retrofit!!.create(serviceClass)
    }

}