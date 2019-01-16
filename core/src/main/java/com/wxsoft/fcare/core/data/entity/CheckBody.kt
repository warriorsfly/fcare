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
        }).append("\t皮肤："+when(skin){
            "1"->"正常"
            "2"->"苍白"
            "3"->"发红"
            "4"->"黄染"
            "5"->"青紫"
            "6"->"湿冷"
            else->""
        }).append(
            "\t左瞳孔："+when(leftPupils) {
                "1" -> "正常"
                "2" -> "扩大"
                "3" -> "缩小"
                "4" -> "不等"
                else -> ""
            }).append(
            "\t左瞳孔对光反应："+when(leftResponseLight) {
                "1" -> "正常"
                "2" -> "迟钝"
                "3" -> "消失"
                else -> ""
            }).append(
            "\t右瞳孔："+when(rightPupils) {
                "1" -> "正常"
                "2" -> "扩大"
                "3" -> "缩小"
                "4" -> "不等"
                else -> ""
            }).append(
            "\t右瞳孔对光反应："+when(rightResponseLight) {
                "1" -> "正常"
                "2" -> "迟钝"
                "3" -> "消失"
                else -> ""
            }).append(if(checkMemo.isNullOrEmpty())"" else "\t备注：$checkMemo").toString()
    }
}
