package com.wxsoft.fcare.core.data.entity.lis

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class LisRecordItem(val id:String) : BaseObservable(){

    @Bindable
    var lisRecordId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.lisRecordId)
        }

    @Bindable
    var itemCode: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.itemCode)
        }

    @Bindable
    var itemName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.itemName)
        }

    @Bindable
    var resultValue: Float = 0.0f
        set(value) {
            field = value
            notifyPropertyChanged(BR.resultValue)
        }

    @Bindable
    var resultChar: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.resultChar)
        }

    @Bindable
    var result: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.result)
        }

    @Bindable
    var referenceAange: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.referenceAange)
        }

    @Bindable
    var hightLowFlag: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.hightLowFlag)
        }

    @Bindable
    var unit: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.unit)
        }

}