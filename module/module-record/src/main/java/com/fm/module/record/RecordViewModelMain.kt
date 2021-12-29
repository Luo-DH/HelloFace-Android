package com.fm.module.record

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fm.library.common.constants.DBFaceMsg
import com.fm.library.face.Utils
import com.fm.library.face.module.Box
import com.fm.library.face.toCropBitmap
import com.fm.library.face.Face
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class RecordViewModelMain(
    private val repository: RecordRepositoryMain
) : ViewModel() {

    private val TAG = "RecordViewModelMain"

    // 人脸检测后的人脸框
    private val _detectBox = MutableLiveData<List<Box>>()
    val detectBox: LiveData<List<Box>> = _detectBox

    private val _feature = MutableLiveData<FloatArray>()
    val feature: LiveData<FloatArray> = _feature


    fun imageToBitmap(image: ImageProxy): Bitmap = repository.imageToBitmap(image)

    fun detectFace(bitmap: Bitmap) {
        val smallBitmap = Utils.scaleBitmap(bitmap, 0.25f)!!
        // 裁剪bitmap
        val cast = measureTimeMillis {
            val res = Face.findFace(smallBitmap).also { it ->
                it.map { m ->
                    m.width = smallBitmap.width
                    m.height = smallBitmap.height
                }
            }
            _detectBox.postValue(res)

            if (res.isNotEmpty()) {
                getFeature(smallBitmap, res[0])
            }
        }
        Log.d(TAG, "detectFace: ${Thread.currentThread()} castTime=$cast")
    }


    /**
     * 人脸识别，获得单个人脸特征值
     */
    fun getFeature(bitmap: Bitmap, box: Box) {

        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val cast = measureTimeMillis {
                    _feature.postValue(
                        Face.getFeature(box.toCropBitmap(bitmap), box)
                    )
                }
                Log.d(TAG, "getFeature: ${Thread.currentThread()} castTime=$cast")
            }
        }
    }

    fun checkFace(feature: FloatArray) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val cast = measureTimeMillis {
                    Log.d(TAG, "hello checkFace: ============================")
                    DBFaceMsg.dbFaceMap.keys.forEach { key ->
                        val fea = DBFaceMsg.dbFaceMap[key]!!
                        val res = Face.checkFace(feature, fea)
                        Log.d(TAG, "hello checkFace: name=$key res=$res")
                    }
                    Log.d(TAG, "hello checkFace: ============================")
                }
            }
        }
    }
}

