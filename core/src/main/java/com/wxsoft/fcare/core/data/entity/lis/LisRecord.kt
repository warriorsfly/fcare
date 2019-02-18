package com.wxsoft.fcare.core.data.entity.lis

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class LisRecord(val id:String) : BaseObservable() {

    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    var applyNo: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.applyNo)
        }

    @Bindable
    var lisItemId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.lisItemId)
        }

    @Bindable
    var lisItemName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.lisItemName)
        }

    @Bindable
    var applyTime: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.applyTime)
        }

    @Bindable
    var excuteTime: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.excuteTime)
        }

    @Bindable
    var endTime: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.endTime)
        }

    @Bindable
    var reportTime: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.reportTime)
        }

    @Bindable
    var lisRecordDetails:List<LisRecordItem> = emptyList()
    set(value) {
        field = value
        notifyPropertyChanged(BR.lisRecordDetails)
    }

}