package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR
import java.io.Serializable

data class NotifyType (
    var templateId:String,
    var descrition:String,
    var messageType:Int,
    var tag:String
): BaseObservable(), Serializable {

    @Transient
    @get:Bindable
    var checked: Boolean = false
    set(value) {
        field = value
        notifyPropertyChanged(BR.checked)
    }

}