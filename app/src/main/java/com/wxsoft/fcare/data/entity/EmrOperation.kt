package com.wxsoft.fcare.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.fcare.BR

data class EmrOperation(val id:String):BaseObservable() {

    /**
     * 来源
     */
    @Bindable
    var actionCode:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.actionCode)
        }

    @Bindable
    var actionName:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.actionName)
        }

    @Bindable
    var moduleCode:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.moduleCode)
        }

    @Bindable
    var operationTime:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.operationTime)
        }
    @Bindable
    var memo:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.memo)
        }
    @Bindable
    var createrId:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.createrId)
        }
    @Bindable
    var createrName:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.createrName)
        }
    @Bindable
    var createdDate:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.createdDate)
        }
    @Bindable
    var modifierId:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.modifierId)
        }
    @Bindable
    var modifierName:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.modifierName)
        }
    @Bindable
    var modifiedDate:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.modifiedDate)
        }

}
