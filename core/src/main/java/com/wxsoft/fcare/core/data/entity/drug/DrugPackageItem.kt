package com.wxsoft.fcare.core.data.entity.drug

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.fcare.core.BR

class DrugPackageItem (val id:String): BaseObservable() {

    @Bindable
    var drugPackageId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugPackageId)
        }

    @Bindable
    var drugId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugId)
        }

    @Bindable
    var drugName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugName)
        }

    @Bindable
    var usage: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.usage)
        }

    @Bindable
    var dose: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.dose)
        }

    @Bindable
    var doseUnit: String =""
        set(value) {
            field = value
            notifyPropertyChanged(BR.doseUnit)
        }


    @Bindable
    var sortNum: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.sortNum)
        }

    @Bindable
    var drug: Drug = Drug("")
        set(value) {
            field = value
            notifyPropertyChanged(BR.drug)
        }


}