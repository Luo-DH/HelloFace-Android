package com.luo.module.init.net

import com.fm.library.common.constants.module.FaceMsg2
import com.luo.module.init.net.domin.FaceMsg
import retrofit2.Response
import retrofit2.http.*

interface InitApi {
    @GET("/test/faces")
    suspend fun getAllFaces2(): Response<List<FaceMsg2>>

    @POST("/test/update-face")
    suspend fun updateFace(
        @Body faceMsg2: FaceMsg,
    ): Response<FaceMsg2>
}