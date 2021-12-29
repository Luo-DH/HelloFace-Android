package com.fm.module.main

import android.content.Context
import android.graphics.Bitmap
import com.fm.library.common.constants.ext.getImageBitmapByUrl
import com.fm.library.common.constants.module.FaceMsg
import com.fm.library.face.FaceSdk

class MainRepositoryMain {

    suspend fun initDB(context: Context, faceList: List<FaceMsg>): HashMap<String, Bitmap> {
        val map = HashMap<String, Bitmap>()
        faceList.forEach { faceMsg ->
            val bitmap = context.getImageBitmapByUrl(faceMsg.imgUrl)
            bitmap?.let {
                map[faceMsg.name] = it
            }
        }
        return map
    }

}