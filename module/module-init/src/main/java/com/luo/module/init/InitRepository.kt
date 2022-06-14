package com.luo.module.init

import android.graphics.Bitmap
import com.fm.library.common.constants.module.FaceMsg2
import com.fm.library.common.constants.net.GlobalServiceCreator
import com.fm.library.face.Face
import com.fm.library.face.toCropBitmap
import com.luo.module.init.net.InitApi
import com.luo.module.init.net.domin.toFaceMsg

class InitRepository {

    val api = GlobalServiceCreator.create<InitApi>()

    suspend fun requestFaces(): Array<ArrayList<FaceMsg2>> {
        val res = api.getAllFaces2()
        if (!res.isSuccessful) return arrayOf(ArrayList(), ArrayList())

        val list = res.body()!!

        val nullList = ArrayList<FaceMsg2>(list.size)
        val notNullList = ArrayList<FaceMsg2>(list.size)

        list.map {
            if (it.fea.isNullOrEmpty()) {
                nullList.add(it)
            } else {
                notNullList.add(it)
            }
        }
        return arrayOf(nullList, notNullList)
    }

    suspend fun initOneFace(bitmap: Bitmap): FloatArray {
        val box = Face.findFace(bitmap)[0]
        return Face.getFeature(box.toCropBitmap(bitmap), box)
    }

    suspend fun updateFace(face: FaceMsg2): FaceMsg2? {
        val updateFace = api.updateFace(face.toFaceMsg())
        return updateFace.body()
    }


}