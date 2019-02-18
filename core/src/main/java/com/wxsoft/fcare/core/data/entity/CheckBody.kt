package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

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
            "1"->"\n皮肤：正常"
            "2"->"\n皮肤：苍白"
            "3"->"\n皮肤：发红"
            "4"->"\n皮肤：黄染"
            "5"->"\n皮肤：青紫"
            "6"->"湿冷"
            else->""
        }).append(
            when(leftPupils) {
                "1" -> "\n左瞳孔：正常"
                "2" -> "\n左瞳孔：扩大"
                "3" -> "\n左瞳孔：缩小"
                "4" -> "\n左瞳孔：不等"
                else -> ""
            }).append(
            when(leftResponseLight) {
                "1" -> "\n左瞳孔对光反应：正常"
                "2" -> "\n左瞳孔对光反应：迟钝"
                "3" -> "\n左瞳孔对光反应：消失"
                else -> ""
            }).append(
            when(rightPupils) {
                "1" -> "\n右瞳孔：正常"
                "2" -> "\n右瞳孔：扩大"
                "3" -> "\n右瞳孔：缩小"
                "4" -> "\n右瞳孔：不等"
                else -> ""
            }).append(
            when(rightResponseLight) {
                "1" -> "\n右瞳孔对光反应：正常"
                "2" -> "\n右瞳孔对光反应：迟钝"
                "3" -> "\n右瞳孔对光反应：消失"
                else -> ""
            }).append(if(checkMemo.isEmpty())"" else "\n备注：$checkMemo").toString()
    }
}
