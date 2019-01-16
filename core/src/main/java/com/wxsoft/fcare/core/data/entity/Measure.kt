package com.wxsoft.fcare.core.data.entity

import com.google.gson.annotations.SerializedName
import java.lang.StringBuilder

data class Measure (@SerializedName("measureDtos")
                    var measures:List<MeasureDic>,
                    @SerializedName("pre_Visit_Result_Code")
                    var preVisitResultCode:String,
                    @SerializedName("pre_Direct_DepartId")
                    var preDirectDepartId:String,
                    @SerializedName("pre_Cure_Result_Code")
                    var preCureResultCode:String
                    ){
//    override fun toString(): String {
//        return StringBuilder().append( when{
//            measures.contains("1")
//            "1"->"正常采集"
//            "2"->"拒绝检测"
//            "3"->"不配合"
//            else->""
//        }).append("\t皮肤："+when(skin){
//            "1"->"正常"
//            "2"->"苍白"
//            "3"->"发红"
//            "4"->"黄染"
//            "5"->"青紫"
//            "6"->"湿冷"
//            else->""
//        }).append(
//            "\t左瞳孔："+when(leftPupils) {
//                "1" -> "正常"
//                "2" -> "扩大"
//                "3" -> "缩小"
//                "4" -> "不等"
//                else -> ""
//            }).append(
//            "\t左瞳孔对光反应："+when(leftResponseLight) {
//                "1" -> "正常"
//                "2" -> "迟钝"
//                "3" -> "消失"
//                else -> ""
//            }).append(
//            "\t右瞳孔："+when(rightPupils) {
//                "1" -> "正常"
//                "2" -> "扩大"
//                "3" -> "缩小"
//                "4" -> "不等"
//                else -> ""
//            }).append(
//            "\t右瞳孔对光反应："+when(rightResponseLight) {
//                "1" -> "正常"
//                "2" -> "迟钝"
//                "3" -> "消失"
//                else -> ""
//            }).append(if(checkMemo.isNullOrEmpty())"" else "\t备注：$checkMemo").toString()
//    }
}