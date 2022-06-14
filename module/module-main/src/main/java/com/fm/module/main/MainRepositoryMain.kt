package com.fm.module.main

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.fm.library.common.constants.ext.getImageBitmapByUrl
import com.fm.library.common.constants.module.FaceMsg
import com.fm.library.common.constants.module.FaceMsg2
import com.fm.library.common.constants.net.GlobalServiceCreator
import com.fm.library.face.FaceSdk
import com.fm.module.main.net.MainNetApi

class MainRepositoryMain {

    suspend fun initDB(context: Context, faceList: List<FaceMsg>): HashMap<String, Bitmap> {
        val map = HashMap<String, Bitmap>()
        faceList.forEach { faceMsg ->
            val bitmap = context.getImageBitmapByUrl(faceMsg.imgUrl)
            bitmap?.let {
                map[faceMsg.name] = it
            }
        }
        return map
    }

    private val mApi = GlobalServiceCreator.create<MainNetApi>()

    suspend fun loadFace(): ArrayList<FaceMsg2> {
        val res = mApi.getAllFaces2()
        val list = ArrayList<FaceMsg2>()
        if (res.isSuccessful) {
            list.addAll(res.body()!!)
        }
        return list
    }

    suspend fun loadFaceWithFea(): HashMap<String, FloatArray> = loadFace().toFea()


}

fun ArrayList<FaceMsg2>.toFea(): HashMap<String, FloatArray> {
    val map = HashMap<String, FloatArray>()
    this.forEach {
        val fl = FloatArray(128)
        val a = it.fea!!.split(" ").subList(0, 128)
        Log.d("vansing", "toFea: size=${a.size}  ${a}")
        a.forEachIndexed { index, fea ->
            fl[index] = fea.toFloat()
        }
        map[it.name!!] = fl
    }
    return map
}