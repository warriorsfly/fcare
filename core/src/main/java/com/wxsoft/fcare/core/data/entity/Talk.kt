package com.wxsoft.fcare.core.data.entity

import android.arch.persistence.room.Ignore
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class Talk (val id:String) : BaseObservable() {

    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    var startTime: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.startTime)
        }

    @Bindable
    var endTime: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.endTime)
        }

    @Bindable
    var informedConsentId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.informedConsentId)
        }

    @Bindable
    var informedConsentName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.informedConsentName)
        }

    @Bindable
    var location: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.location)
        }

    @Bindable
    @Transient
    var patientInfo: Patient = Patient("")
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientInfo)
        }

    @Ignore
    var attachments:List<Attachment> = emptyList()

    @Bindable
    var informedConsent: InformedConsent = InformedConsent("")
        set(value) {
            field = value
            notifyPropertyChanged(BR.informedConsent)
        }

    @Bindable
    var informedTime:String = "07:24:20"
        set(value) {
            field = value
            notifyPropertyChanged(BR.informedTime)
        }

    fun judgeTime(){

    }

}