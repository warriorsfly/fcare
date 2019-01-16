package com.wxsoft.fcare.core.data.entity

import com.google.gson.annotations.SerializedName

data class MeasureDic (val id:String,
                       val patientId:String,
                       val measureCode:String,
                       @SerializedName("measureCode_Name")
                       val measureName:String=""
                       )