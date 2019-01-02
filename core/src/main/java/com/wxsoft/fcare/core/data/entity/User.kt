package com.wxsoft.fcare.core.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class User(
    var id:String,
    var userName: String ,
    var password: String ,
    var trueName: String ,
    var tel: String ,
    var email: String ,
    var weiXin: String ,
    var deptId: String ,
    var deptName: String ,
    var postId: String ,
    var isActive: Boolean,
    var postName: String ,
    var hospitalId: String ,
    var hospitalName: String ,
    var memo: String ,
    var jgAccount: String ,
    var userRoles: Array<UserRole> ,
    var createdDate: String ,
    var modifiedDate: String ,
    var createrId: String ,
    var createrName: String ,
    var modifierId: String ,
    var modifierName: String): BaseObservable() {

    @Transient
    @get:Bindable
    var status: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.status)
        }

}