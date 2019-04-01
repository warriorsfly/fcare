package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class NotiUserItem (
    var id:String,
    var name:String,
    var memo:String,
    var hospitalId:String,
    var diagnosisCode:String,
    var users:List<User>
): BaseObservable() {

    @Bindable
    @Transient
    var checked: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.checked)
        }

}