package com.fm.module.main

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fm.library.common.constants.net.GlobalServiceCreator
import com.fm.library.face.Face
import com.fm.library.face.FaceSdk
import com.fm.library.face.module.Box
import com.fm.library.face.toCropBitmap
import com.fm.module.main.net.MainNetApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class MainViewModelMain(
    val context: Context,
    val repository: MainRepositoryMain
) : ViewModel() {

    private val _dbFaceMap = MutableLiveData<Map<String, Bitmap>>()
    val dbFaceMap: LiveData<Map<String, Bitmap>> = _dbFaceMap

    private val _dbFeaMap = MutableLiveData<Map<String, FloatArray>>()
    val dbFeaMap: LiveData<Map<String, FloatArray>> = _dbFeaMap

    val api = GlobalServiceCreator.create<MainNetApi>()

    fun loadFace() {
        viewModelScope.launch(Dispatchers.IO) {
            val rsp = api.getAllFaces()
            if (rsp.isSuccessful && rsp.body()!!.code == 0) {
                // 获取人脸后，后台进行数据初始化
                _dbFaceMap.postValue(repository.initDB(context, rsp.body()!!.res))
            }
        }
    }

    fun initFace(faceMap: Map<String, Bitmap>) {
        viewModelScope.launch(Dispatchers.Default) {
            val map = HashMap<String, FloatArray>()
            faceMap.keys.forEach { key ->
                val bitmap = faceMap[key]!!
                val res = Face.findFace(bitmap).also { it ->
                    it.map { m ->
                        m.width = bitmap.width
                        m.height = bitmap.height
                    }
                }
                if (res.isNotEmpty()) {
                    val box = res[0]
                    val feature = Face.getFeature(box.toCropBitmap(bitmap), box)
                    map[key] = feature
                }
            }
            _dbFeaMap.postValue(map)
        }
    }


}