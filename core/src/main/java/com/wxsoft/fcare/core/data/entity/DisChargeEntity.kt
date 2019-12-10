package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class DisChargeEntity (val id:String=""): BaseObservable(){

    /// <summary>
    /// 病人id
    /// </summary>
    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }
    /// <summary>
    ///
    /// </summary>
    @Bindable
    var enumItemId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.enumItemId)
        }
    /// <summary>
    ///
    /// </summary>
    @Bindable
    var enumItemName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.enumItemName)
        }
    /// <summary>
    ///
    /// </summary>
    @Bindable
    var enumItemId1: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.enumItemId1)
        }
    /// <summary>
    ///
    /// </summary>
    @Bindable
    @SerializedName("isChoose")
    var hasChoose: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.hasChoose)
        }
}