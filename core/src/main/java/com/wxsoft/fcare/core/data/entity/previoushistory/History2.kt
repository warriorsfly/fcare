package com.wxsoft.fcare.core.data.entity.previoushistory

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class History2(val id:String)
    : BaseObservable() {
    var drugId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugId)
        }
    var drugName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugName)
        }
    var doseUnit: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.doseUnit)
        }
//    var medicalHistoryId: String = ""
//        set(value) {
//            field = value
//            notifyPropertyChanged(BR.medicalHistoryId)
//        }

    @Bindable
    var dose: Float = 0f
        set(value) {
            field = value
            notifyPropertyChanged(BR.dose)
        }
}
