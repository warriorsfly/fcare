package com.wxsoft.fcare.core.data.entity

import android.arch.persistence.room.Ignore
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
//        get() {
//            if (field == null)
//                return field
//
//            return field?.substring(11,16)
//        }
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
//        get() {
//            if (field == null)
//                return field
//
//            return field?.substring(11,16)
//        }
        set(value) {
            field = value
            notifyPropertyChanged(BR.firstMet)
        }

    @get:Bindable
    var returnAt: String? = ""
//        get() {
//            if (field == null)
//                return field
//
//            return field?.substring(11,16)
//        }
        set(value) {

            field = value
            notifyPropertyChanged(BR.returnAt)
        }
    @get:Bindable
    var arriveHosAt: String? = ""
//        get() {
//            if (field == null)
//                return field
//
//            return field?.substring(11,16)
//        }

        set(value) {
            field = value

            notifyPropertyChanged(BR.arriveHosAt)
        }
    @get:Bindable
    var carId: String? = ""
    var canceled: String? = ""
    var taskStaffs: Array<TaskStaff> = emptyArray()
    @Ignore
    var patients: Array<Patient> = emptyArray()
    @SerializedName("isCanceled")var hasCanceled: Boolean = false

    var createdBy: String? = ""
    var createdDate: String? = "0001-01-01 00:00:00"
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
    var process= if(status==5)8 else 2*status-1

//    @Transient
//    @get:Bindable
//    var arrivalTime:Int?= null
//        get() {
//            return getLastMinutes(startTimeStamp,arriveTimeStamp)?.toInt()
//        }
//
//    @Transient
//    @get:Bindable
//    var editTime:Long?= null
//        get() {
//            return getLastMinutes(arriveTimeStamp,firstMetTimeStamp)
//        }
//
//    @Transient
//    @get:Bindable
//    var returningTime:Long?= null
//        get() {
//            return getLastMinutes(firstMetTimeStamp,returningTimeStamp)
//        }
//
//    @Transient
//    @get:Bindable
//    var arriveHosTime:Long?= null
//        get() {
//            return getLastMinutes(returningTimeStamp,arriveHosTimeStamp)
//        }
}
//TODO:delete useless propeties