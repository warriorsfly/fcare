package com.wxsoft.fcare.core.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.fcare.core.BR
import java.lang.StringBuilder
import kotlin.system.measureTimeMillis

data class CheckBody (val id:String): BaseObservable(){

    @Bindable
    var coordination: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.coordination)
        }
    @Bindable
    var skin: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.skin)
        }
    @Bindable
    var leftPupils: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.leftPupils)
        }
    @Bindable
    var leftResponseLight: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.leftResponseLight)
        }
    @Bindable
    var rightPupils: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.rightPupils)
        }
    @Bindable
    var rightResponseLight: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.rightResponseLight)
        }
    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    var checkMemo: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.checkMemo)
        }

    @Bindable
    var createdDate: String = "0001-01-01 00:00:00"
        set(value) {
            field = value
            notifyPropertyChanged(BR.createdDate)
        }



    override fun toString(): String {
        return StringBuilder().append( when(coordination){
            "1"->"正常采集"
            "2"->"拒绝检测"
            "3"->"不配合"
            else->""
        }).append(when(skin){
            "1"->"\t\t皮肤：正常"
            "2"->"\t\t皮肤：苍白"
            "3"->"\t\t皮肤：发红"
            "4"->"\t\t皮肤：黄染"
            "5"->"\t\t皮肤：青紫"
            "6"->"湿冷"
            else->""
        }).append(
            when(leftPupils) {
                "1" -> "\t\t左瞳孔：正常"
                "2" -> "\t\t左瞳孔：扩大"
                "3" -> "\t\t左瞳孔：缩小"
                "4" -> "\t\t左瞳孔：不等"
                else -> ""
            }).append(
            when(leftResponseLight) {
                "1" -> "\t\t左瞳孔对光反应：正常"
                "2" -> "\t\t左瞳孔对光反应：迟钝"
                "3" -> "\t\t左瞳孔对光反应：消失"
                else -> ""
            }).append(
            when(rightPupils) {
                "1" -> "\t\t右瞳孔：正常"
                "2" -> "\t\t右瞳孔：扩大"
                "3" -> "\t\t右瞳孔：缩小"
                "4" -> "\t\t右瞳孔：不等"
                else -> ""
            }).append(
            when(rightResponseLight) {
                "1" -> "\t\t右瞳孔对光反应：正常"
                "2" -> "\t\t右瞳孔对光反应：迟钝"
                "3" -> "\t\t右瞳孔对光反应：消失"
                else -> ""
            }).append(if(checkMemo.isNullOrEmpty())"" else "\t\t备注：$checkMemo").toString()
    }
}
