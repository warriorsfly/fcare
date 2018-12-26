package com.wxsoft.fcare.core.data.entity

import com.google.gson.annotations.SerializedName
import java.sql.Date

data class Car(var id:String,
               var name:String,
               @SerializedName("linkMan")var linkTo:String,
               @SerializedName("isHospitalCar")var belongHospital: Boolean,
               @SerializedName("registeredYear")var registeredYear: Date,
               @SerializedName("statu")var status: Int,
               @SerializedName("isEnable")var enabled: Boolean)