package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class Strategy (val id:String=""): BaseObservable() {
    @Bindable
    var patientId: String = ""
        set(value) {

            field = value
            notifyPropertyChanged(BR.patientId)
        }
    @Bindable
    var strategyCode: String = ""
        set(value) {

            field = value
            notifyPropertyChanged(BR.strategyCode)
        }
    @Bindable
    var strategyCode_Name: String = ""
        set(value) {

            field = value
            notifyPropertyChanged(BR.strategyCode_Name)
        }
    @Bindable
    var createdDate: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.createdDate)
        }



    override fun toString(): String {
        return StringBuilder().append(strategyCode_Name).toString()
    }
}