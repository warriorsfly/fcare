package com.wxsoft.fcare.core.data.entity

import android.databinding.ObservableField
import com.google.gson.annotations.SerializedName

class BodyCheck(
    val id:String,
    var patientId:ObservableField<String>,
    var skin:ObservableField<String>,
    @SerializedName("leftPupils")var leftEyePupils:ObservableField<String>,
    @SerializedName("leftResponseLight")var leftEyeLight:ObservableField<String>,
    @SerializedName("rightPupils")var rightEyePupils:ObservableField<String>,
    @SerializedName("rightResponseLight")var rightEyeLight:ObservableField<String>,
    @SerializedName("checkMemo")var memo:ObservableField<String>,
    var createdBy: ObservableField<String>,
    var createdDate: ObservableField<String>,
    var modifiedBy: ObservableField<String>,
    var modifiedDate: ObservableField<String>
)