package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class TravelTimeLine (val id:String=""): BaseObservable() {

    //病人信息
    @Bindable
    var patientInfoDto: Patient = Patient("")
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientInfoDto)
        }
    var triggerRecordLists:List<TriggerRecord> = emptyList()

    fun getDate():String?{
        return triggerRecordLists.first().getDate()
    }
    fun timeStr(){
        triggerRecordLists.map {
            it.getStr()
        }
    }

}