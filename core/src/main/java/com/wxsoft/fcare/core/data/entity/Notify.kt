package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class Notify (
    var senderUserId:String,
    var senderUserName:String,
    var patientId:String,
    var patientName:String,
    var receiverUserIds:List<String>
): BaseObservable() {

    @Bindable
    var messageTemplateName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.messageTemplateName)
        }

}