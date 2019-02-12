package com.wxsoft.fcare.core.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.utils.DateTimeUtils

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

    @Bindable
    var ah:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.ah)
        }

//    @Bindable
//    var medicalHistoryPhotos:ArrayList<String> = emptyList()
//        set(value) {
//            field=value
//            notifyPropertyChanged(BR.medicalHistoryPhotos)
//        }

    @Bindable
    var attachments:List<Attachment>  = emptyList()
        set(value) {
            field=value
            notifyPropertyChanged(BR.attachments)
        }
    @Bindable
    var createdDate:String=DateTimeUtils.getCurrentTime()
        set(value) {
            field=value
            notifyPropertyChanged(BR.createdDate)
        }


}