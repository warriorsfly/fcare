package com.wxsoft.fcare.core.data.entity.lis

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class LisJCRecord (val id:String) : BaseObservable(){

    //项目名称
    @Bindable
    var jylbmc: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.jylbmc)
        }
    //送检日期
    @Bindable
    var sjrq: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.sjrq)
        }
    //报告日期
    @Bindable
    var bgrq: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.bgrq)
        }

    @Bindable
    var wcsj: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.wcsj)
        }
    //检查所见
    @Bindable
    var jcsj: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.jcsj)
        }
    //检查部位
    @Bindable
    var jcbw: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.jcbw)
        }
    //检查结论
    @Bindable
    var jcjl: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.jcjl)
        }
    //送检结论
    @Bindable
    var sjjl: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.sjjl)
        }

}