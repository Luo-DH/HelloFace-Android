package com.luo.module.init

import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import java.lang.StringBuilder

@RequiresApi(Build.VERSION_CODES.O)
fun FloatArray.toBase64(): String {
    val sb = StringBuilder()
    this.forEach {
        sb.append("$it ")
    }
    return Base64.encodeToString(sb.toString().encodeToByteArray(), Base64.NO_WRAP)
}

fun FloatArray.toStringList(): String {
    val sb = StringBuilder()
    this.forEach {
        sb.append("$it ")
    }
    return sb.toString()
}