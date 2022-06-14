package com.fm.module.record

import android.graphics.*
import android.util.Log
import androidx.camera.core.ImageProxy
import com.fm.library.common.constants.DBFaceMsg
import com.fm.library.common.constants.QRCodeMsg
import com.fm.library.common.constants.module.FaceMsg2
import com.fm.library.common.constants.module.FaceRsp
import com.fm.library.common.constants.net.GlobalServiceCreator
import com.fm.module.record.net.RecordNetApi
import retrofit2.Response
import java.io.ByteArrayOutputStream

class RecordRepositoryMain {

    fun imageToBitmap(image: ImageProxy): Bitmap {
        val nv21 = imageToNV21(image)
        val yuvImage =
            YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(
            Rect(
                0,
                0,
                yuvImage.width,
                yuvImage.height
            ), 100, out
        )
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }


    private fun imageToNV21(image: ImageProxy): ByteArray {
        val planes = image.planes
        val y = planes[0]
        val u = planes[1]
        val v = planes[2]
        val yBuffer = y.buffer
        val uBuffer = u.buffer
        val vBuffer = v.buffer
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        val nv21 = ByteArray(ySize + uSize + vSize)
        // U and V are swapped
        yBuffer[nv21, 0, ySize]
        vBuffer[nv21, ySize, vSize]
        uBuffer[nv21, ySize + vSize, uSize]
        return nv21
    }

    val api = GlobalServiceCreator.create<RecordNetApi>()

    suspend fun requestBitmap(name: String): Response<FaceMsg2> {
        return api.getFaceBitmap(name)
    }

    suspend fun updateData(name: String) {
        api.updateData11111(name)
    }

    suspend fun reqestMsg() {
        val res  = api.getFaceBitmap(QRCodeMsg.name)
        QRCodeMsg.name = ""
        if (res.isSuccessful) {
            val list = ArrayList<FaceMsg2>(1).apply {
                this.add(res.body()!!)
            }
            DBFaceMsg.dbFaceMap = list.toFea()
        } else {
            DBFaceMsg.dbFaceMap = DBFaceMsg.dbFaceMap2
        }
    }

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