package com.fm.module.record.sdk

import android.graphics.Bitmap
import com.fm.library.face.FaceSdk
import com.fm.library.face.Utils
import com.fm.library.face.module.Box

/**
 * 封装FaceSdk
 */
object Face {

    init {
        FaceSdk // 初始化模型
    }

    /* 人脸检测方法 */
    fun findFace(bitmap: Bitmap): List<Box> = FaceSdk.FindFace.detect(bitmap)

    fun getFeature(bitmap: Bitmap, box: Box): FloatArray {
        val landmarks = IntArray(10)
        var i = 0
        box.landmarks.forEach {
            landmarks[i] = it.x.toInt() - box.x1
            landmarks[i + 5] = it.y.toInt() - box.y1
            i += 1
        }
        return FaceSdk.CheckFace.getFeature(
            Utils.getPixelsRGBA(bitmap), bitmap.width, bitmap.height, landmarks
        )
    }

    fun checkFace(feature1: FloatArray, feature2: FloatArray): Float {
        return FaceSdk.CheckFace.getResult(feature1, feature2)
    }

}