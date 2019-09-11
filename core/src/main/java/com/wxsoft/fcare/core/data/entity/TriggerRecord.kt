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
    var outTime: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.outTime)
        }

    @Bindable
    var startOrEndFlag: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.startOrEndFlag)
        }

    @Bindable
    var costTime: Int = 0
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

    fun getTime1():String{
        return inTime?.substring(11,16)?:"--:--"
    }
    fun getTime2():String{
        return outTime?.substring(11,16)?:"--:--"
    }
    fun getAllCostTime():String{
        return costTime?.toString()
    }
    fun getDate():String{
        return inTime?.substring(0,10)?:"--:--"
    }

}