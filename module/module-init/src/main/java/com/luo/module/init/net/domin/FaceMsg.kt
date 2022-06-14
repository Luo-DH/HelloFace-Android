package com.luo.module.init.net.domin

import com.fm.library.common.constants.module.FaceMsg2
import java.sql.Date


data class FaceMsg (
    var id: Long? = null,
    var name: String? = null,
    var pic: String? = null,
    var date: Date? = null,
    var last: Date? = null,
    var fea: String? = null
)

fun FaceMsg2.toFaceMsg(): FaceMsg {
    val faceMsg = FaceMsg(
        id, name, pic, fea = fea
    )
    val data = Date(System.currentTimeMillis())
//    faceMsg.date = data
//    faceMsg.last = data
    return faceMsg
}
