package com.wxsoft.fcare.core.data.entity

data class EmrImage
    (
        val id: String="",
        val fileName: String="",
        val fileType: String="",
        val fileSize: Long=0L,
        val physicalRelativePath: String="",
        val thumbnailId: String="",
        val httpUrl: String="",
        val createrId: String?=null,
        val createrName: String?=null,
        val createdDate: String?=null,
        var modifierId: String?=null,
        var modifierName: String?=null,
        var modifiedDate: String?=null
)