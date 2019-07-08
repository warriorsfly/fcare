package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class CheckBody (val id:String): BaseObservable(){

    @Bindable
    var coordination: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.coordination)
        }
    @SerializedName("coordination_Name")
    @Bindable
    var coordinationName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.coordinationName)
        }
    @Bindable
    var skin: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.skin)
        }
    @SerializedName("skin_Name")
    @Bindable
    var skinName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.skinName)
        }
    @Bindable
    var leftPupils: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.leftPupils)
        }
    @SerializedName("leftPupils_Name")
    @Bindable
    var leftPupilsName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.leftPupilsName)
        }
    @Bindable
    var leftResponseLight: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.leftResponseLight)
        }
    @SerializedName("leftResponseLight_Name")
    @Bindable
    var leftResponseLightName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.leftResponseLightName)
        }
    @Bindable
    var rightPupils: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.rightPupils)
        }
    @SerializedName("rightPupils_Name")
    @Bindable
    var rightPupilsName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.rightPupilsName)
        }
    @Bindable
    var rightResponseLight: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.rightResponseLight)
        }
    @SerializedName("rightResponseLight_Name")
    @Bindable
    var rightResponseLightName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.rightResponseLightName)
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
    var createdDate: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.createdDate)
        }



    override fun toString(): String {
        return StringBuilder().append( when(coordination){
            "206-1"->"正常采集"
            "206-2"->"拒绝检测"
            "206-3"->"不配合"
            else->""
        }).append(when(skin){
            "207-1"->"\n皮肤：正常"
            "207-2"->"\n皮肤：苍白"
            "207-3"->"\n皮肤：发红"
            "207-4"->"\n皮肤：黄染"
            "207-5"->"\n皮肤：青紫"
            "207-6"->"湿冷"
            else->""
        }).append(
            when(leftPupils) {
                "208-1" -> "\n左瞳孔：正常"
                "208-2" -> "\n左瞳孔：扩大"
                "208-3" -> "\n左瞳孔：缩小"
                "208-4" -> "\n左瞳孔：不等"
                else -> ""
            }).append(
            when(leftResponseLight) {
                "209-1" -> "\n左瞳孔对光反应：正常"
                "209-2" -> "\n左瞳孔对光反应：迟钝"
                "209-3" -> "\n左瞳孔对光反应：消失"
                else -> ""
            }).append(
            when(rightPupils) {
                "208-1" -> "\n右瞳孔：正常"
                "208-2" -> "\n右瞳孔：扩大"
                "208-3" -> "\n右瞳孔：缩小"
                "208-4" -> "\n右瞳孔：不等"
                else -> ""
            }).append(
            when(rightResponseLight) {
                "209-1" -> "\n右瞳孔对光反应：正常"
                "209-2" -> "\n右瞳孔对光反应：迟钝"
                "209-3" -> "\n右瞳孔对光反应：消失"
                else -> ""
            }).append(if(checkMemo.isEmpty())"" else "\n备注：$checkMemo").toString()
    }
}
