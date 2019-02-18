package com.wxsoft.fcare.core.data.entity.drug

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class DrugPackage  (val id:String): BaseObservable() {

    @Bindable
    var name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @Bindable
    var sortNum: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.sortNum)
        }

    @Bindable
    var drugPackageDetails: List<DrugPackageItem> = emptyList()
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugPackageDetails)
        }

    @Bindable
    @Transient
    var checked: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.checked)
        }





}