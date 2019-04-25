package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class Passing(val id:String="",
                   val patientId:String,
                   val createrId	:String,
                   val createrName	:String?=null,
                   val createdDate	:String?=null,
                   var modifierId	:String?=null,
                   var modifierName	:String?=null,
                   var modifiedDate	:String?=null
):BaseObservable(){
    @Bindable
    @SerializedName("is_Bypass_Emergency")
    var passingEmergency:Boolean=false
        set(value) {
            if(value)
                passingCCU=false
            field=value

            notifyPropertyChanged(BR.passingEmergency)
        }

    @Bindable
    @SerializedName("bypass_Emergency_Code")
    var passingEmergencyCode:String?=null
        set(value) {
            field=value
            notifyPropertyChanged(BR.passingEmergencyCode)
        }

    @Bindable
    @SerializedName("bypass_Emergency_Code_Name")
    var passingEmergencyName:String?=null
        set(value) {
            field=value
            notifyPropertyChanged(BR.passingEmergencyName)
        }

    @Bindable
    @SerializedName("bypass_Emergency_Leave_Time")
    var passingEmergencyTime:String?=null
        set(value) {
            field=value
            notifyPropertyChanged(BR.passingEmergencyTime)
        }

    @Bindable
    @SerializedName("arrived_Emergency_Time")
    var arriveEmergencyTime:String?=null
        set(value) {
            field=value
            notifyPropertyChanged(BR.arriveEmergencyTime)
        }

    @Bindable
    @SerializedName("is_Bypass_Ccu")
    var passingCCU:Boolean=false
        set(value) {
            if(passingEmergency)return
            field=value
            notifyPropertyChanged(BR.passingCCU)
        }

    @Bindable
    @SerializedName("arrived_Ccu_Date")
    var arriveCCUTime:String?=null
        set(value) {
            field=value
            notifyPropertyChanged(BR.arriveCCUTime)
        }
}