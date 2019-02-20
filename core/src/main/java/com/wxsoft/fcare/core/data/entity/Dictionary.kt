package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

/**
 * modified by warriorsfly 2019-02-20
 * 依据api变动，移除itemcode属性，原itemcode->id
 */
class Dictionary(val id:String, val itemName:String): BaseObservable() {


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