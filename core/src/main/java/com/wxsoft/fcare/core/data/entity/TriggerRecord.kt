package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class TriggerRecord (val id:String=""): BaseObservable() {

    @Bindable
    var baseStationId: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.baseStationId)
        }

    @Bindable
    var baseStationName: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.baseStationName)
        }

    @Bindable
    var inTime: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.inTime)
        }

    @Bindable
    @Transient
    var inTimeStr: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.inTimeStr)
        }

    @Bindable
    var outTime: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.outTime)
        }

    @Bindable
    @Transient
    var outTimeStr: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.outTimeStr)
        }

    @Bindable
    var startOrEndFlag: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.startOrEndFlag)
        }

    @Bindable
    var costTime: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.costTime)
        }

    @Bindable
    @SerializedName("isPass")
    var pass: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.pass)
        }
    fun getDate():String{
        return inTime?.substring(0,10)?:"--:--"
    }
    fun getStr(){
        inTimeStr = inTime?.substring(11,16)?:"--:--"
        outTimeStr = outTime?.substring(11,16)?:"--:--"
    }

}