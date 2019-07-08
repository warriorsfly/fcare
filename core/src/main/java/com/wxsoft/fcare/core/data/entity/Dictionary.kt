package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR
import java.io.Serializable

/**
 * modified by warriorsfly 2019-02-20
 * 依据api变动，移除itemcode属性，原itemcode->drugId
 */
data class Dictionary(val id:String, val itemName:String): BaseObservable(), Serializable {


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
    var enumDictId:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.enumDictId)
        }

    @Bindable
    var memo:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.memo)
        }

    @Bindable
    var sortNum:Int=0
        set(value) {
            field=value
            notifyPropertyChanged(BR.sortNum)
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
    @Bindable
    @Transient
    var position:Int=0
        set(value) {
            field=value
            notifyPropertyChanged(BR.position)
        }

    fun selected(){
        checked = !checked
    }

}