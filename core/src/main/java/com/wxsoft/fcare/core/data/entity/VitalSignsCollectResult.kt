package com.wxsoft.fcare.core.data.entity

open class VitalSignsCollectResult (
    /**
     *
     */
    val timePoint:String,
    val strTime :String,
    val heart_Rate:Int,
    val dbp:Int,
    val sbp:Int,
    val patientId:String,
    val isAutomatic:Boolean,
    val deviceId:String,
    val vitalSignsCollectPlanId:String,
    val createrId:String,
    val id:String

)