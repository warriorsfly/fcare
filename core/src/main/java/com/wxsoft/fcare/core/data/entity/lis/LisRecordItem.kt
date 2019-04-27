package com.wxsoft.fcare.core.data.entity.lis

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class LisRecordItem(val id:String) : BaseObservable(){

    //项目名称
    @Bindable
    var xmmc: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.xmmc)
        }
    //项目结果
    @Bindable
    var xmjg: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.xmjg)
        }
    //项目单位
    @Bindable
    var xmdw: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.xmdw)
        }
    //参考范围
    @Bindable
    var jgckz: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.jgckz)
        }
    //高低标志
    @Bindable
    var gdbz: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.gdbz)
        }

}