package com.fm.module.manager.net

import com.fm.library.common.constants.module.CommonRsp
import com.fm.library.common.constants.module.FaceHistoryRsp
import com.fm.library.common.constants.module.FaceMsg2
import com.fm.library.common.constants.module.FaceRsp
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ManagerStoreApi {

    @GET("/get_face")
    suspend fun getAllFaces(): Response<FaceRsp>

    @GET("/test/faces")
    suspend fun getAllFaces2(): Response<List<FaceMsg2>>

    @GET("/get_history")
    suspend fun getAllHistoryData(): Response<FaceHistoryRsp>

    @Multipart
    @POST("/upload")
    suspend fun upload(@Part part: MultipartBody.Part): Response<CommonRsp>

}