package com.fm.helloface

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface TestApi {

//    @GET("fm/navigate/list")
    @GET("/test")
    suspend fun testGet(): Response<UserTest>

    @POST("/upload")
    suspend fun testUpload(): Response<UserTest>

    @Multipart
    @POST("/upload")
    suspend fun testUploadFile(@Part part: MultipartBody.Part)

}