package com.luo.library.net

import com.luo.library.net.interceptor.LoggingInterceptor
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class ServiceCreator(
    private val baseUrl: String
) {

//    val httpClient = OkHttpClient.Builder()
//        .addInterceptor(MultiBaseUrlInterceptor())
//        .addInterceptor(LoggingInterceptor())
//        .addInterceptor(HeaderInterceptor())
//        .addInterceptor(SaveCookieInterceptor())
//        .addInterceptor(CacheInterceptor())
//        .cache(cache)
//        .addInterceptor(BasicParamsInterceptor())
//        .build()

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(LoggingInterceptor())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

}