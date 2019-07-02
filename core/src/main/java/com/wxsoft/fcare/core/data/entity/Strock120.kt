package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class Strock120(
    val id:String="",
    val createrId:String,
    val createrName:String
): BaseObservable() {
    @Bindable
    var isFace:Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.isFace)
        }
    @Bindable
    var isArm:Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.isArm)
        }
    @Bindable
    var isSpeak:Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.isSpeak)
        }
}