package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class TaskSpend(val task:String):BaseObservable(){
    var spends:Long?=null
    set(value) {
        field=value
        field?.let {
            spending= """${(it / 60)}分${(it % 60)}秒"""
        }
    }

    @get:Bindable
    var spending=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.spending)
        }
}