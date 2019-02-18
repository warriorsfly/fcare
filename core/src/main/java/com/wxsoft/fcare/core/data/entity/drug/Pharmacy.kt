package com.wxsoft.fcare.core.data.entity.drug

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class Pharmacy (val id:String): BaseObservable() {

    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    var drugBagChecked: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugBagChecked)
        }

}