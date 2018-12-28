package com.wxsoft.fcare.core.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class Task (val id:String): BaseObservable(){
    var taskDate: String? = ""
    @Bindable
    var taskPosition: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.taskPosition)
        }
    var startAt: String? = ""
    var arriveAt: String? = ""
    var firstMet: String? = ""
    var returnAt: String? = ""
    var arriveHosAt: String? = ""
    var carId: String? = ""
    var canceled: String? = ""
    var taskStaffs: Array<TaskStaff> = emptyArray()
    var patients: Array<Patient> = emptyArray()
    @SerializedName("isCanceled")var hasCanceled: Boolean = false

    var createdBy: String? = ""
    var createdDate: String? = ""
    var modifiedBy: String? = ""
    var modifiedDate: String? = ""
}