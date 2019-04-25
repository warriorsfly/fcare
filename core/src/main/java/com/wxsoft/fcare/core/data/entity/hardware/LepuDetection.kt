package com.wxsoft.fcare.core.data.entity.hardware

data class LepuDetection (
    val no:String,
    val name:String,
    val detectionTime:String,
    val detectionItems:List<Item>,
    val syncTime:Int
)