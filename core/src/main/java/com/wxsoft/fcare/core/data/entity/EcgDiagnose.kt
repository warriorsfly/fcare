package com.wxsoft.fcare.core.data.entity

import com.google.gson.annotations.SerializedName

data class EcgDiagnose(
    @SerializedName("ecgId")
    val id:String,
    @SerializedName("ecgDiagnoseCode")
    val code:String,
    @SerializedName("ecgDiagnoseCode_Name")
    val name:String)