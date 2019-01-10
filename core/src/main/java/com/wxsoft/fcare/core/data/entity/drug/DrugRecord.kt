package com.wxsoft.fcare.core.data.entity.drug

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class DrugRecord (val id:String) : BaseObservable() {

    @Bindable
    var drugRecordId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugRecordId)
        }

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
    var dose: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.dose)
        }

    @Bindable
    var drug: Drug = Drug("")
        set(value) {
            field = value
            notifyPropertyChanged(BR.drug)
        }

    @Bindable
    var drugPackage: DrugPackage = DrugPackage("")
        set(value) {
            field = value
            notifyPropertyChanged(BR.drug)
        }


}