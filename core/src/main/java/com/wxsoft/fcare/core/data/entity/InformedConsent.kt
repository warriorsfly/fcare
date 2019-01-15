package com.wxsoft.fcare.core.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class InformedConsent(val id:String) : BaseObservable() {

    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    var checked: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.checked)
        }

    @Bindable
    var spand: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.spand)
        }


    fun select(){
        spand = !spand
    }

    fun checkedItem(){
        checked = !checked
    }
}