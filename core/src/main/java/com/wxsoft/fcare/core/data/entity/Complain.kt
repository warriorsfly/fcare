package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR
import java.sql.Date

/**
 * 主诉
 */
data class Complain(val id:String
): BaseObservable() {

    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    var ccCode: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ccCode)
        }

    @Bindable
    var ccCode_Name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ccCode_Name)
        }

    @Bindable
    var createdBy: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.createdBy)
        }

    @Bindable
    var createdDate: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.createdDate)
        }

    @Bindable
    var modifiedBy: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedBy)
        }

    @Bindable
    var modifiedDate: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }


}