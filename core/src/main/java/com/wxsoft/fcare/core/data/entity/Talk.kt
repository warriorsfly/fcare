package com.wxsoft.fcare.core.data.entity

import android.arch.persistence.room.Ignore
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.utils.DateTimeUtils

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

    @Transient
    @Bindable
    var allTime:String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.allTime)
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
    var informedTime:String = DateTimeUtils.getCurrentTime()
        set(value) {
            field = value
            notifyPropertyChanged(BR.informedTime)
        }
    @Bindable
    var createdDate:String? = null
    set(value) {
        field = value
        notifyPropertyChanged(BR.createdDate)
    }


    fun judgeTime(){
        allTime = DateTimeUtils.getAAfromBBMinutes(startTime,endTime)
    }

}