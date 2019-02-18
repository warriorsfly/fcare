package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

class Dictionary(val id:String, val itemName:String, val itemCode:String): BaseObservable() {


    /**
     *
     */
    @Bindable
    var patientId:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    @Transient
    var checked:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.checked)
        }

    @Bindable
    @Transient
    var section:Int=0
        set(value) {
            field=value
            notifyPropertyChanged(BR.section)
        }

    fun selected(){
        checked = !checked
    }

}