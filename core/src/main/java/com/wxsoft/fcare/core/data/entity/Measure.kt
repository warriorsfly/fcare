package com.wxsoft.fcare.core.data.entity

import com.google.gson.annotations.SerializedName
import java.lang.StringBuilder

data class Measure (@SerializedName("measureDtos")
                    var measureDtos:List<MeasureDic> = emptyList(),
                    @SerializedName("pre_Visit_Result_Code")
                    var preVisitResultCode:String?=null,
                    @SerializedName("pre_Direct_DepartId")
                    var preDirectDepartId:String?=null,
                    @SerializedName("pre_Cure_Result_Code")
                    var preCureResultCode:String?=null,
                    @SerializedName("pre_Cure_Result_Code_Name")
                    var preCureResultName:String?=null,
                    var createDate:String?=null
                    ){
    override fun toString(): String {
        return StringBuilder().append( if(measureDtos.isNullOrEmpty()) "" else ("治疗措施:"+
                measureDtos.joinToString (separator = ",",transform = {it.measureName}))
             ).toString()
    }



}