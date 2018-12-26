package com.wxsoft.fcare.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.fcare.BR
import com.wxsoft.fcare.data.entity.chest.Evaluation


/**
 * 病人信息
 */
//@Entity(tableName = "emrs")
data class Emr( val id:String,  @Bindable val patient: Patient): BaseObservable() {

    @Bindable var address:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.address)
        }
    /**
     * 来院方式
     */
    @Bindable var transferBy:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.transferBy)
        }

    /**
     * 急救类型
     */
    @Bindable var firstAidType:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.transferBy)
        }

    /**
     * 急救开始时间
     */
    @Bindable var aidStart:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.aidStart)
        }

    @Bindable var evaluations:List<Evaluation> =ArrayList()
}