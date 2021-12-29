package com.fm.module.main.net

import com.fm.library.common.constants.module.CommonRsp
import com.fm.library.common.constants.module.FaceRsp
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MainNetApi {

    @GET("/get_face")
    suspend fun getAllFaces(): Response<FaceRsp>

    @Multipart
    @POST("/upload")
    suspend fun upload(@Part part: MultipartBody.Part): Response<CommonRsp>

}