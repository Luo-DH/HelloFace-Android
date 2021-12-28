package com.fm.library.face

import android.content.res.AssetManager
import android.graphics.Bitmap
import com.fm.library.face.module.Box

/**
 * 人脸识别sdk
 */
object FaceSdk {

    init {
        System.loadLibrary("face")
    }


    /**
     * 人脸检测方案
     */
    object FindFace {

        //加载模型接口 AssetManager用于加载assert中的权重文件
        external fun init(mgr: AssetManager): Boolean

        //模型检测接口,其值=4-box + 5-landmark
        external fun detect(bitmap: Bitmap): List<Box>

    }

    /**
     * 人脸识别方案
     */
    object CheckFace {

        /**
         * 初始化模型
         */
        external fun init(manager: AssetManager)

        /**
         * 获得单张图片的特征值
         */
        external fun getFeature(
            faceDate: ByteArray,
            width: Int,
            height: Int,
            landmarks: IntArray
        ): FloatArray

        /**
         * 特征值比对, 获得最终结果
         */
        external fun getResult(feature1: FloatArray, feature2: FloatArray): Float

    }


}