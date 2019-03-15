package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class EmrCase<T>(val items:List<Record<T>>):BaseObservable(){

    private var index:Int?=null
        set(value) {
            field=value
            field?.let{
                current=items[it]
            }
        }

    init {
        if(items.isNotEmpty()){
            index=0
        }
    }

    @get:Bindable
    var current:Record<T>?=null
    set(value) {
        field=value
        notifyPropertyChanged(BR.current)
    }

    fun change(index:Int){
        this.index=index
    }
}