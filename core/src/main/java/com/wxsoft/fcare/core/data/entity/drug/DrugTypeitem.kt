package com.wxsoft.fcare.core.data.entity.drug

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class DrugTypeitem(val id:String): BaseObservable() {

    @Bindable
    var drugTypeId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugTypeId)
        }

    @Bindable
    var drugTypeName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugTypeName)
        }

    @Bindable
    var drugItems: List<Drug> = emptyList()
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugItems)
        }

    @Bindable
    @Transient
    var checked: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.checked)
        }
}