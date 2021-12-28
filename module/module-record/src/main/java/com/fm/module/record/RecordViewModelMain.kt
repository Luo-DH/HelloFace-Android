package com.fm.module.record

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel

class RecordViewModelMain(
    private val repository: RecordRepositoryMain
) : ViewModel() {

    fun imageToBitmap(image: ImageProxy): Bitmap = repository.imageToBitmap(image)


}