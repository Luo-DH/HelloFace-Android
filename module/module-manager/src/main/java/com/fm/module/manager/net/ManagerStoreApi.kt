package com.fm.module.manager.net

import com.fm.library.common.constants.module.FaceHistoryRsp
import com.fm.library.common.constants.module.FaceRsp
import retrofit2.Response
import retrofit2.http.GET

interface ManagerStoreApi {

    @GET("/get_face")
    suspend fun getAllFaces(): Response<FaceRsp>

    @GET("/get_history")
    suspend fun getAllHistoryData(): Response<FaceHistoryRsp>

}