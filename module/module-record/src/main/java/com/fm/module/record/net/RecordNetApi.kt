package com.fm.module.record.net

import com.fm.library.common.constants.module.FaceHistoryRsp
import com.fm.library.common.constants.module.FaceRsp
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecordNetApi {


    @GET("get_face")
    suspend fun getFaceBitmap(@Query("name") name: String): Response<FaceRsp>

    @GET("create_history")
    suspend fun updateData11111(@Query("name") name: String): Response<Any>

    @GET("get_history")
    suspend fun getHistory(): Response<FaceHistoryRsp>
}