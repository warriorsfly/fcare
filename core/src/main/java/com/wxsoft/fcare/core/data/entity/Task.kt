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

    @get:Bindable
    var startAt: String? = ""
    get() {
        if (field == null)
            return field

        return field?.substring(11,16)
    }
        set(value) {
            field = value
            notifyPropertyChanged(BR.startAt)
        }
    @get:Bindable
    var arriveAt: String? = ""
        get() {
            if (field == null)
                return field

            return field?.substring(11,16)
        }
        set(value) {
            field = value
            notifyPropertyChanged(BR.arriveAt)
        }
    @get:Bindable
    var firstMet: String? = ""
        get() {
            if (field == null)
                return field

            return field?.substring(11,16)
        }
        set(value) {
            field = value
            notifyPropertyChanged(BR.firstMet)
        }
    @get:Bindable
    var returnAt: String? = ""
        get() {
            if (field == null)
                return field

            return field?.substring(11,16)
        }
        set(value) {
            field = value
            notifyPropertyChanged(BR.returnAt)
        }
    @get:Bindable
    var arriveHosAt: String? = ""
        get() {
            if (field == null)
                return field

            return field?.substring(11,16)
        }

        set(value) {
            field = value
            notifyPropertyChanged(BR.arriveHosAt)
        }
    @get:Bindable
    var carId: String? = ""
    var canceled: String? = ""
    var taskStaffs: Array<TaskStaff> = emptyArray()
    var patients: Array<Patient> = emptyArray()
    @SerializedName("isCanceled")var hasCanceled: Boolean = false

    var createdBy: String? = ""
    var createdDate: String? = ""
    var modifiedBy: String? = ""
    var modifiedDate: String? = ""

    @get:Bindable
    @SerializedName("taskStatu")var status=0
        set(value) {

            field = value
            notifyPropertyChanged(BR.status)
            notifyPropertyChanged(BR.process)

        }

    @Transient
    @get:Bindable
    var process:Int= 0
        get() {
            return if(status==5)8 else 2*status-1
        }
}