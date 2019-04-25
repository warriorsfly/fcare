package com.wxsoft.fcare.core.data.entity.hardware

data class MindrayDetection (
    val id:String,
    val ip:String,
    val deviceName:String,
    val description:String,
    val detectionTime:String,
    val spO2:Int,
    val pulseRate:Int,
    val bloodPressureSystolic:Int,
    val bloodPressureDiastolic:Int,
    val syncTime:String

)