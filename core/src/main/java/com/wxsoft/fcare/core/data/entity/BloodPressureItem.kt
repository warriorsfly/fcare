package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class BloodPressureItem(val id:String?= ""): BaseObservable() {

    @Bindable
    var timePoint: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.timePoint)
        }
    @Bindable
    var strTime: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.strTime)
        }
    @Bindable
    @SerializedName("heart_Rate")
    var heartRate: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.heartRate)
        }
    @Bindable
    var dbp: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.dbp)
        }
    @Bindable
    var sbp: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.sbp)
        }
    var patientId: String? = null
    var isAutomatic: Boolean = false
    var deviceId: String? = null
    var vitalSignsCollectPlanId: String? = null
    var createdDate: String? = null
    var modifiedDate: String? = null
    var createrId: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.createrId)
        }
    var createrName: String? = null
    var modifierId: String? = null
    var modifierName: String? = null

    
}