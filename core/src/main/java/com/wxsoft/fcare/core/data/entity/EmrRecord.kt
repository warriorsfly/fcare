package com.wxsoft.fcare.core.data.entity

data class EmrRecord(
    val typeId:String="",
    val typeName:String="",
    val items: List<EmrImage>,
    var tint:Int=0,
    var patientId:String?,
    var currUserId:String?)
//):Record<EmrImage>(typeId,typeName,items,tint)