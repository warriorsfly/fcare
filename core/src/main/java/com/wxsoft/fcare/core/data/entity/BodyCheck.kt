package com.wxsoft.fcare.core.data.entity

import com.google.gson.annotations.SerializedName

class BodyCheck(
    val id:String,
    var patientId:String,
    var skin:String,
    @SerializedName("leftPupils")var leftEyePupils:String,
    @SerializedName("leftResponseLight")var leftEyeLight:String,
    @SerializedName("rightPupils")var rightEyePupils:String,
    @SerializedName("rightResponseLight")var rightEyeLight:String,
    @SerializedName("checkMemo")var memo:String,
    var createdBy: String,
    var createdDate: String?,
    var modifiedBy: String,
    var modifiedDate: String
)