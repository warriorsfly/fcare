package com.wxsoft.fcare.core.data.entity

open class VitalSignsCollectExcutePlan (
    val id:String,
    val patientId:String,
    val startTime:String,
    val endTime:String,
    val deviceId:String,
    val createrId:String,
    val status:Int,
    val devices:List<Device>
)