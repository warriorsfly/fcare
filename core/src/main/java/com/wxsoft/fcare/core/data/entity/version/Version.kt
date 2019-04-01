package com.wxsoft.fcare.core.data.entity.version

import com.google.gson.annotations.SerializedName

data class Version(
    @SerializedName("hasUpdate")
    val changing:Boolean=false,
    @SerializedName("apkUrl")
    val url:String="",
    @SerializedName("updateDescription")
    val description:String="",
    @SerializedName("apkSizeAsByte")
    val size:Int=0,
    @SerializedName("mD5")
    val md5:String=""
)