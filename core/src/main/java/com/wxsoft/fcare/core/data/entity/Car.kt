package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class Car(var id:String,
               var name:String,
               @SerializedName("linkMan")var linkTo:String,
               @SerializedName("isHospitalCar")var belongHospital: Boolean,
               @SerializedName("registeredYear")var registeredYear: String,
               @SerializedName("statu_Name")var statuName: String,
               @SerializedName("isEnable")var enabled: Boolean):BaseObservable() {

    @SerializedName("statu")
    @get:Bindable
    var status: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.status)
        }

    @Transient
    @Bindable
    var selectStatus: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectStatus)
        }

}