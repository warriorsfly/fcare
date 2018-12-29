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
    get() {
        if (field == null)
            return field

        return field?.substring(11,16)
    }
    var arriveAt: String? = ""
        get() {
            if (field == null)
                return field

            return field?.substring(11,16)
        }
    var firstMet: String? = ""
        get() {
            if (field == null)
                return field

            return field?.substring(11,16)
        }
    var returnAt: String? = ""
        get() {
            if (field == null)
                return field

            return field?.substring(11,16)
        }
    var arriveHosAt: String? = ""
        get() {
            if (field == null)
                return field

            return field?.substring(11,16)
        }
    var carId: String? = ""
    var canceled: String? = ""
    var taskStaffs: Array<TaskStaff> = emptyArray()
    var patients: Array<Patient> = emptyArray()
    @SerializedName("isCanceled")var hasCanceled: Boolean = false

    var createdBy: String? = ""
    var createdDate: String? = ""
    var modifiedBy: String? = ""
    var modifiedDate: String? = ""

    var status=-1

    fun getProcess():Int= status*2+1

}