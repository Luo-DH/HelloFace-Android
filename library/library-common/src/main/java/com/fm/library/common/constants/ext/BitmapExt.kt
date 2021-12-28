package com.fm.library.common.constants.ext

import android.graphics.Bitmap
import android.graphics.Matrix

/**
 * 将图片旋转90度
 */
fun Bitmap.toRotaBitmap(): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(-90F)
    return Bitmap.createBitmap(
        this,
        0, 0,
//        Math.min(this.width, this.height),
//        Math.min(this.width, this.height),
        width, height,
        matrix,
        false
    )
}