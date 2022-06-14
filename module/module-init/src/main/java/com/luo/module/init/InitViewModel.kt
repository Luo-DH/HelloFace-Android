package com.luo.module.init

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fm.library.common.constants.module.FaceMsg2
import com.fm.library.face.Face
import com.fm.library.face.toCropBitmap
import com.luo.module.init.net.domin.toFaceMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InitViewModel : ViewModel() {

    private val mRepository = InitRepository()

    private val _noFeaLiveData = MutableLiveData<ArrayList<FaceMsg2>>()
    val noFeaLiveData: LiveData<ArrayList<FaceMsg2>> = _noFeaLiveData

    private val _haveFeaLiveData = MutableLiveData<ArrayList<FaceMsg2>>()
    val haveFeaLiveData: LiveData<ArrayList<FaceMsg2>> = _haveFeaLiveData


    private val _needToUpdate = MutableLiveData(false)
    val needToUpdate: LiveData<Boolean> = _needToUpdate

    /**
     * 获取人脸库的信息
     *
     * .. 信息应当包括
     * ... 根据人脸列表，遍历fea字段，null/!null两个list
     * ... 从!null的list中取人脸进行识别
     * ... 识别后update服务器中数据
     * ... 服务器返回最新的列表
     * ... 更新本地null/!null两个list
     */
    fun requestFaces() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = mRepository.requestFaces()
            _noFeaLiveData.postValue(res[0])
            _haveFeaLiveData.postValue(res[1])
        }
    }

    fun initFace(face: FaceMsg2, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.Default) {
            val fea = mRepository.initOneFace(bitmap)
            face.fea = fea.toStringList()
            val a=mRepository.updateFace(face)
            if (a != null) {
                _needToUpdate.postValue(true)
            } else {
                _needToUpdate.postValue(false)
            }
        }
    }

    fun initFace(face: List<FaceMsg2>, bitmaps: List<Bitmap>) {
        viewModelScope.launch(Dispatchers.Default) {
            for (i in face.indices) {
                _needToUpdate.postValue(false)
                val fea = mRepository.initOneFace(bitmaps[i])
                face[i].fea = fea.toStringList()
                mRepository.updateFace(face[i])
                _needToUpdate.postValue(true)
            }
        }
    }
}