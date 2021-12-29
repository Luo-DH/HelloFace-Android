package com.fm.module.record

import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
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
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.max
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

    private val _voteRes = MutableLiveData<String>()
    val voteRes: LiveData<String> = _voteRes

    private val _voteMap = HashMap<String, Int>()

    // 识别结果 空代表陌生人
    private val _recognizeRes = MutableLiveData<String?>()
    val recognizeRes: LiveData<String?> = _recognizeRes

    // 要展示的图片的地址
    private val _imgUrl = MutableLiveData<String>()
    val imgUrl: LiveData<String?> = _imgUrl


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
                var maxRes = 0f
                var maxKey: String? = null
                Log.d(TAG, "hello checkFace: ============================")
                DBFaceMsg.dbFaceMap.keys.forEach { key ->
                    val fea = DBFaceMsg.dbFaceMap[key]!!
                    val res = Face.checkFace(feature, fea)
                    Log.d(TAG, "hello checkFace: name=$key res=$res")
                    if (res > 0.65 && res > maxRes) {
                        maxRes = res
                        maxKey = key
                    }
                }
                Log.d(TAG, "hello checkFace: ============================")

                _recognizeRes.postValue(maxKey)
            }
        }
    }

    fun vote(name: String?) {
        synchronized(this) {
            val n = name ?: "unknown"
            val num = _voteMap[n]
            if (num == null) {
                _voteMap[n] = 1
            } else {
                _voteMap[n] = num + 1
            }

            getMostVote()
        }
    }

    private fun getMostVote() {
        var maxVote = 10
        var maxKey = ""
        _voteMap.keys.forEach { key ->
            val num = _voteMap[key]!!
            if (num > maxVote) {
                maxVote = num
                maxKey = key
            }
        }
        if (maxKey != "") {
            _voteRes.postValue(maxKey)
        }
    }

    fun clearVoteMap() {
        synchronized(this) {
            _voteMap.clear()
        }
    }

    fun requestBitmap(name: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (name == null && name == "unknown") {
                _imgUrl.postValue("https://cdn.pixabay.com/photo/2021/10/11/00/59/warning-6699085_1280.png")
            } else {
                val res = repository.requestBitmap(name!!)
                if (res.isSuccessful) {
                    _imgUrl.postValue(res.body()!!.res[0].imgUrl)
                }
            }
        }
    }

    fun updateData(name: String, mBitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }


}

