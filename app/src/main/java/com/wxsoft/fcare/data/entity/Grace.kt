package com.wxsoft.fcare.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.BR

data class Grace  constructor( var id:String=""): BaseObservable() {

    /**
     * 发病后首次出现心脏骤停
     */
    @Bindable
    @SerializedName("is_Arrest")
    var arrest:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.arrest)
        }

    /**
     * 心电图ST段改变
     */
    @Bindable
    @SerializedName("is_Change")
    var change:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.change)
        }
    /**
     * 心肌坏死标志物升高
     */
    @Bindable
    @SerializedName("is_Rise")
    var rise:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.rise)
        }

    /**
     * 分值
     */
    @Bindable
    var grace_Value:Int=120
        set(value) {
            field=value
            notifyPropertyChanged(BR.grace_Value)
        }

    /**
     * 分值
     */
    @Transient
    @Bindable
    var gracevalue:String="0"
        set(value) {
            field=value
            if (!field.equals(""))grace_Value = field.toInt()
            notifyPropertyChanged(BR.gracevalue)
        }

    /**
     * 危险程度
     */
    @Bindable
    var risk_Lamination:String="3"
        set(value) {
            field=value
            notifyPropertyChanged(BR.risk_Lamination)
        }
    /**
     * 病人id
     */
    @Bindable
    var patientId:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.patientId)
        }

    fun setCheckedValue(){
        gracevalue = grace_Value.toString()
    }

}