package com.fm.module.manager.net

import retrofit2.Response
import retrofit2.http.GET

interface ManagerStoreApi {

    @GET("/get_face")
    suspend fun getAllFaces(): Response<ManagerRsp>

}