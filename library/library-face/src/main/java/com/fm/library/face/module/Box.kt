package com.fm.library.face.module

import android.graphics.PointF

data class Box(
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int,

    var width: Int,
    var height: Int,

    val landmarks: Array<PointF>
)