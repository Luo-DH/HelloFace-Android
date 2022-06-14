package com.fm.library.common.constants.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult

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


/**
 * 从图片地址获取Bitmap
 * @receiver Context
 * @param url String
 * @return Bitmap?
 */
suspend fun Context.getImageBitmapByUrl(url: String): Bitmap? {
    val request = ImageRequest.Builder(this)
        .data(url)
        .allowHardware(false)
        .build()
    val result = (imageLoader.execute(request) as SuccessResult).drawable
    return (result as BitmapDrawable).bitmap
}
