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
    //申请单号
    @Bindable
    var reportNo: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.reportNo)
        }
    //检验类别
    @Bindable
    var jylbmc: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.jylbmc)
        }
    //采样日期
    @Bindable
    var cyrq: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.cyrq)
        }
    //送检日期
    @Bindable
    var sjrq: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.sjrq)
        }
    //检验日期
    @Bindable
    var jyrq: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.jyrq)
        }
    //报告日期
    @Bindable
    var bgrq: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.bgrq)
        }
    //发布日期
    @Bindable
    var fbrq: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.fbrq)
        }
    //检验报告明细项
    @Bindable
    var lisReoprtaRecordDetails:List<LisRecordItem> = emptyList()
    set(value) {
        field = value
        notifyPropertyChanged(BR.lisReoprtaRecordDetails)
    }

}