package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.utils.DateTimeUtils

data class MedicalHistory(val id:String): BaseObservable() {

    @Bindable
    var patientId:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    var provide:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.provide)
        }

    @Bindable
    var cc:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.cc)
        }

    @Bindable
    var hpi:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.hpi)
        }

    @Bindable
    var ph:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.ph)
        }

    @Bindable
    @SerializedName("pH_Name")
    var phName:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.phName)
        }

    //过敏史
    @Bindable
    var ah:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.ah)
        }

    @Bindable
    var attachments:List<Attachment>  = emptyList()
        set(value) {
            field=value
            notifyPropertyChanged(BR.attachments)
        }
    @Bindable
    var createdDate:String= DateTimeUtils.getCurrentTime()
        set(value) {
            field=value
            notifyPropertyChanged(BR.createdDate)
        }


}