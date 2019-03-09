package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class PatientsCondition (
                        var pageIndex:Int,
                        var pageSize:Int
): BaseObservable(){

    @Bindable
    var keyword:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.keyword)
        }
    @Bindable
    var startDate:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.startDate)
        }
    @Bindable
    var endDate:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.endDate)
        }
    @Bindable
    var diagnoseType:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnoseType)
        }
    @Transient
    @Bindable
    var diagnoseTypeStr:String="全部类型"
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnoseTypeStr)
        }

    @Transient
    @Bindable
    var checkedCusDate:Boolean=false
        set(value) {
            field = value
            notifyPropertyChanged(BR.checkedCusDate)
        }

    @Transient
    @Bindable
    var checkedCusDateStr:String="今日"
        set(value) {
            field = value
            notifyPropertyChanged(BR.checkedCusDateStr)
        }




}