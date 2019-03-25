package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class InformedConsent(val id:String) : BaseObservable() {

    @Bindable
    var name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @Bindable
        var informedType: String? = ""
            set(value) {
                field = value
                notifyPropertyChanged(BR.informedType)
            }

    @Bindable
    @SerializedName("informedType_Name")
    var informedTypeName: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.informedTypeName)
        }


    @Bindable
        var content: String = ""
            set(value) {
                field = value
                notifyPropertyChanged(BR.content)
            }







}