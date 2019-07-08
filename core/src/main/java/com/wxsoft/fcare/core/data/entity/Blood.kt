package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class Blood(var id:String="",
                 var patientId:String):BaseObservable() {

    @Bindable
    var takingTime: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.takingTime)
        }

    @Bindable
    var sendCheckTime: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.sendCheckTime)
        }

    @Bindable
    var bloodSugarCompleteTime: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.bloodSugarCompleteTime)
        }

    @Bindable
    var bloodSugarResult: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.bloodSugarResult)
        }


    @Bindable
    var createrId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.createrId)
        }
    @Bindable
    var createrName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.createrName)
        }
    @Bindable
    var createdDate: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.createdDate)
        }
    @Bindable
    var modifierId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifierId)
        }
    @Bindable
    var modifierName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifierName)
        }
    @Bindable
    var modifiedDate: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

}