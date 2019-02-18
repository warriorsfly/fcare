package com.wxsoft.fcare.core.data.entity.drug

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class Drug (val id:String): BaseObservable() {

    @Bindable
    var name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @Bindable
    var usage: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.usage)
        }

    @Bindable
    var specifications: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.specifications)
        }

    @Bindable
    var dose: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.dose)
        }
    @Bindable
    @Transient
    var doseNum: String = ""
        set(value) {
            field = value
            dose = if(value != "") value.toInt() else 0
            notifyPropertyChanged(BR.doseNum)
        }

    @Bindable
    var doseUnit: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.doseUnit)
        }

    @Bindable
    @SerializedName("isEnable")
    var hasEnable: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.hasEnable)
        }


    @Bindable
    @Transient
    var checked: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.checked)
        }

    @Bindable
    @Transient
    var bagdrug: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.bagdrug)
        }


}