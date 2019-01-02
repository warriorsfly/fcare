package com.wxsoft.fcare.core.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.utils.DateTimeUtils
import com.wxsoft.fcare.utils.getLastMinutes

data class Task (val id:String): BaseObservable(){
    var taskDate: String? = ""
    @Bindable
    var taskPosition: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.taskPosition)
        }


    @Transient
    private var startTimeStamp:Long?=null
    @Transient
    private var arriveTimeStamp:Long?=null
    @Transient
    @get:Bindable
    var firstMetTimeStamp:Long?=null
    private set(value) {
        field=value
    }
    @Transient
    private var returningTimeStamp:Long?=null
    @Transient
    private var arriveHosTimeStamp:Long?=null

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
            arriveTimeStamp=DateTimeUtils.formatter.parse(value)?.time
            notifyPropertyChanged(BR.arrivalTime)
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
            firstMetTimeStamp=DateTimeUtils.formatter.parse(value)?.time
            notifyPropertyChanged(BR.editTime)
            notifyPropertyChanged(BR.firstMetTimeStamp)
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
            returningTimeStamp=DateTimeUtils.formatter.parse(value)?.time
            notifyPropertyChanged(BR.returningTime)
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

            arriveHosTimeStamp=DateTimeUtils.formatter.parse(value)?.time
            notifyPropertyChanged(BR.arriveHosTime)
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


    @Transient
    @get:Bindable
    var arrivalTime:Long?= null
        get() {
            return getLastMinutes(startTimeStamp,arriveTimeStamp)
        }

    @Transient
    @get:Bindable
    var editTime:Long?= null
        get() {
            return getLastMinutes(arriveTimeStamp,firstMetTimeStamp)
        }

    @Transient
    @get:Bindable
    var returningTime:Long?= null
        get() {
            return getLastMinutes(firstMetTimeStamp,returningTimeStamp)
        }

    @Transient
    @get:Bindable
    var arriveHosTime:Long?= null
        get() {
            return getLastMinutes(returningTimeStamp,arriveHosTimeStamp)
        }
}