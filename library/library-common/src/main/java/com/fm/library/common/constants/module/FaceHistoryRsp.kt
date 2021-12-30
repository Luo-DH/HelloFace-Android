package com.fm.library.common.constants.module

data class FaceHistoryRsp(
    val code: Int,
    val res: List<FaceHistorySubRsp>
)

data class FaceHistorySubRsp(
    val name: String,
    val time: String
)
