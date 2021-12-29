package com.fm.module.main.net

import com.fm.library.common.constants.module.FaceRsp
import retrofit2.Response
import retrofit2.http.GET

interface MainNetApi {

    @GET("/get_face")
    suspend fun getAllFaces(): Response<FaceRsp>

}