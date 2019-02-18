package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.utils.DateTimeUtils

data class CABG (val id:String): BaseObservable() {

    @Bindable
    var patientId:String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    @SerializedName("decision_Operation_Time")
    var decisionOperationTime: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.decisionOperationTime)
        }

    @Bindable
    @SerializedName("start_Operation_Time")
    var startOperationTime: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.startOperationTime)
        }

    @Bindable
    @SerializedName("end_Operation_Time")
    var endOperationTime: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.endOperationTime)
        }

    @Bindable
    var createdDate: String = DateTimeUtils.getCurrentTime()
        set(value) {
            field = value
            notifyPropertyChanged(BR.createdDate)
        }




}