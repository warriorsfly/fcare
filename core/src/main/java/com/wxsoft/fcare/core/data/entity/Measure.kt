package com.wxsoft.fcare.core.data.entity

import com.google.gson.annotations.SerializedName
import java.lang.StringBuilder

data class Measure (@SerializedName("measureDtos")
                    var measureDtos:List<MeasureDic> = emptyList(),
                    @SerializedName("pre_Visit_Result_Code")
                    var preVisitResultCode:String?,
                    @SerializedName("pre_Direct_DepartId")
                    var preDirectDepartId:String?,
                    @SerializedName("pre_Cure_Result_Code")
                    var preCureResultCode:String?,
                    var createDate:String?=null
                    ){
    override fun toString(): String {
        return StringBuilder().append( if(measureDtos.isNullOrEmpty()) "" else ("治疗措施:"+
                measureDtos.joinToString (separator = ",",transform = {it.measureName}))
             ).append(when(preCureResultCode){
            "213-1"->"\n救治结果：好转"
            "213-2"->"\n救治结果：无变化"
            "213-3"->"\n救治结果：加重"
            "213-4"->"\n救治结果：到达死亡"
            "213-5"->"\n救治结果：抢救无效死亡"
            "213-6"->"\n救治结果：途中死亡"
            else->""
        }).append(when(preVisitResultCode) {
                "214-1" -> "\n出诊结果：拒绝治疗"
                "214-2" -> "\n出诊结果：现场治疗"
                "214-3" -> "\n出诊结果：送往医院"
                "214-4" -> "\n出诊结果：拒绝进院"
                "214-5" -> "\n出诊结果：送往上级医院"
                else -> ""
            }).append(
            when(preDirectDepartId) {
                "5-1" -> "\n绕行：导管室"
                "5-2" -> "\n绕行：CCU"
                "5-3" -> "\n绕行：心内科病房"
                null->""
                else -> "\n绕行：其他"
            }).toString()
    }



}