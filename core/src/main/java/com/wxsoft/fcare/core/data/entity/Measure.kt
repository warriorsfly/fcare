package com.wxsoft.fcare.core.data.entity

import com.google.gson.annotations.SerializedName

data class Measure (@SerializedName("measureDtos")var measures:List<MeasureDic>,
                    @SerializedName("pre_Visit_Result_Code")var preVisitResultCode:String,
                    @SerializedName("pre_Cure_Result_Code")var preCureResultCode:String
                    )