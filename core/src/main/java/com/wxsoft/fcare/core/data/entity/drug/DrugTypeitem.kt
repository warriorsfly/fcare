package com.wxsoft.fcare.core.data.entity.drug

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class DrugTypeitem(val id:String): BaseObservable() {

    @Bindable
    var typeId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.typeId)
        }

    @Bindable
    var typeName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.typeName)
        }

    @Bindable
    var items: List<Drug> = emptyList()
        set(value) {
            field = value
            notifyPropertyChanged(BR.items)
        }

    @Bindable
    @Transient
    var checked: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.checked)
        }
}