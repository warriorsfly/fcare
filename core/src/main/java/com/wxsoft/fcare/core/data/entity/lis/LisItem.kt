package com.wxsoft.fcare.core.data.entity.lis

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class LisItem (val id:String) : BaseObservable() {

    @Bindable
    var lisOrderCode: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.lisOrderCode)
        }

    @Bindable
    var hisOrderCode: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.hisOrderCode)
        }

    @Bindable
    var hisOrderName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.hisOrderName)
        }

    @Bindable
    @SerializedName("isPreHospital")
    var preHospital: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.preHospital)
        }

    @Bindable
    @SerializedName("isPOCT")
    var poct: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.poct)
        }

    @Bindable
    @SerializedName("isEmptyRecordDetail")
    var emptyRecordDetail: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.emptyRecordDetail)
        }

    @Bindable
    @Transient
    var checked:Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.checked)
        }


}