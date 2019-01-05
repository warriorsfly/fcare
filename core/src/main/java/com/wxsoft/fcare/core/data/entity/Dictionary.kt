package com.wxsoft.fcare.core.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.fcare.core.BR

class Dictionary(val id:String, val itemName:String, val itemCode:String): BaseObservable() {


    /**
     * 来源
     */
    @Bindable
    var patientId:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    var checked:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.checked)
        }

    @Bindable
    var section:Int=0
        set(value) {
            field=value
            notifyPropertyChanged(BR.section)
        }

}