package com.wxsoft.fcare.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.fcare.BR

data class OperationMenu(val id:String):BaseObservable(){

    @Bindable
    var name:String=""
        set(value) {

            field = value
            notifyPropertyChanged(BR.name)
        }
    @Bindable
    var parentId:String=""
        set(value) {

            field = value
            notifyPropertyChanged(BR.parentId)
        }

    @Bindable
    var iconClass:String=""
        set(value) {

            field = value
            notifyPropertyChanged(BR.iconClass)
        }
  @Bindable
    var sortNum:Int=1
        set(value) {

            field = value
            notifyPropertyChanged(BR.sortNum)
        }

    @Bindable
    var controller:String=""
        set(value) {

            field = value
            notifyPropertyChanged(BR.controller)
        }

    @Bindable
    var actionCode:String=""
        set(value) {

            field = value
            notifyPropertyChanged(BR.actionCode)
        }

    @Bindable
    var actionName:String=""
        set(value) {

            field = value
            notifyPropertyChanged(BR.actionName)
        }

    @Bindable
    var hasExcuted:Boolean=false
        set(value) {

            field = value
            notifyPropertyChanged(BR.hasExcuted)
        }

//    @Bindable
//    var parameter:String=""
//        set(value) {
//
//            field = value
//            notifyPropertyChanged(BR.parameter)
//        }
//
//    @Bindable
//    var parent:OperationMenu?=null
//        set(value) {
//
//            field = value
//            notifyPropertyChanged(BR.parent)
//        }
//
//    @Bindable
//    var childMenus:List<OperationMenu>?=null
//        set(value) {
//
//            field = value
//            notifyPropertyChanged(BR.childMenus)
//        }
//
//    @Bindable
//    var createrId:String=""
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.createrId)
//        }
//    @Bindable
//    var createrName:String=""
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.createrName)
//        }
//    @Bindable
//    var createdDate:String=""
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.createdDate)
//        }
//    @Bindable
//    var modifierId:String=""
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.modifierId)
//        }
//    @Bindable
//    var modifierName:String=""
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.modifierName)
//        }
//    @Bindable
//    var modifiedDate:String=""
//        set(value) {
//
//            field=value
//            notifyPropertyChanged(BR.modifiedDate)
//        }
}