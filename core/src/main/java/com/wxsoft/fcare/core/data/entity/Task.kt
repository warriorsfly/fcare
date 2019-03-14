package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
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
        set(value) {
            field = value
            notifyPropertyChanged(BR.startAt)
        }


    @get:Bindable
    var arriveAt: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.arriveAt)
        }

    @get:Bindable
    var firstMet: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.firstMet)
        }

    @get:Bindable
    var returnAt: String? = ""
        set(value) {

            field = value
            notifyPropertyChanged(BR.returnAt)
        }
    @get:Bindable
    var arriveHosAt: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.arriveHosAt)
        }
    @get:Bindable
    var carId: String? = ""
    var canceled: String? = ""
    var taskStaffs: Array<TaskStaff> = emptyArray()

    var patients: List<Patient> = emptyList()
    @SerializedName("isCanceled")var hasCanceled: Boolean = false

    var createdBy: String? = ""
    var createdDate: String? = "0001-01-01 00:00:00"
    var modifiedBy: String? = ""
    var modifiedDate: String? = ""
    var taskOverallStatu: Int? = 1

    @get:Bindable
    @SerializedName("taskStatu")var status=0
        set(value) {
            field = value
            process=if(field==5)8 else 2*field-1
            notifyPropertyChanged(BR.status)
        }

    @Transient
    var spends = mutableListOf<TaskSpend>()

    @Transient
    @get:Bindable
    var process= 0
    set(value) {
        field=value
        notifyPropertyChanged(BR.process)
    }

}