package com.wxsoft.fcare.core.data.entity

import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR
import java.lang.StringBuilder

data class Measure (@SerializedName("measureDtos")
                    var measures:List<MeasureDic> = emptyList(),
                    @SerializedName("pre_Visit_Result_Code")
                    var preVisitResultCode:String?,
                    @SerializedName("pre_Direct_DepartId")
                    var preDirectDepartId:String?,
                    @SerializedName("pre_Cure_Result_Code")
                    var preCureResultCode:String?,
                    var createdDate:String?="2019-01-19 12:00:00"
                    ){
    override fun toString(): String {
        return StringBuilder().append( if(measures.isNullOrEmpty()) "" else ("治疗措施:"+
            measures.joinToString (separator = ",",transform = {it.measureName}))
             ).append(when(preVisitResultCode){
            "1"->"\t\t救治结果：好转"
            "2"->"\t\t救治结果：无变化"
            "3"->"\t\t救治结果：加重"
            "4"->"\t\t救治结果：到达死亡"
            "5"->"\t\t救治结果：抢救无效死亡"
            "6"->"\t\t救治结果：途中死亡"
            else->""
        }).append(when(preVisitResultCode) {
                "1" -> "\t\t出诊结果：拒绝治疗"
                "2" -> "\t\t出诊结果：现场治疗"
                "3" -> "\t\t出诊结果：送往医院"
                "4" -> "\t\t出诊结果：拒绝进院"
                "5" -> "\t\t出诊结果：送往上级医院"
                else -> ""
            }).append(
            when(preDirectDepartId) {
                "1" -> "\t\t绕行：导管室"
                "2" -> "\t\t绕行：CCU"
                "3" -> "\t\t绕行：心内科病房"
                null->""
                else -> "\t\t绕行：其他"
            }).toString()
    }



}