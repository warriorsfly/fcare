package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR
import java.io.Serializable


data class Tag (
    val id:String="",
    var alias:String="",
    var hospitalId:String="",
    var isEnable:Boolean=true,
    var isUsing:Boolean=false,
    var memo	:String?=null,
    var currentPatientId	:String?=null,
    var currentBaseStationId	:String?=null

): BaseObservable(), Serializable {
    @Transient
    @get:Bindable
    var checked: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.checked)
        }

}