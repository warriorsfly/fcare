package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class Department(val id:String):BaseObservable(){

    @Bindable
    var selected:Boolean=true
        set(value) {

            field=value
            notifyPropertyChanged(BR.selected)
        }

    @Bindable
    var name:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.name)
        }
}