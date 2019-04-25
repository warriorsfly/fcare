package com.wxsoft.fcare.core.data.entity.hardware

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class MindrayDevices (
    val id:String,
    val name:String,
    val no:String,
    val ip:String,
    val port:Int,
    val description:String,
    val type:String): BaseObservable() {

    @Transient
    @Bindable
    var checked: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.checked)
        }

}