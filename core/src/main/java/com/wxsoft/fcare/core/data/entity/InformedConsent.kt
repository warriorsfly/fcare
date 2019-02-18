package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class InformedConsent(val id:String) : BaseObservable() {

    @Bindable
    var name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @Bindable
        var informedType: Int = 0
            set(value) {
                field = value
                notifyPropertyChanged(BR.informedType)
            }

    @Bindable
        var content: String = ""
            set(value) {
                field = value
                notifyPropertyChanged(BR.content)
            }







}