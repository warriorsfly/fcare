package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class SignInUser(var id:Int,
                      var name:String,
                      var userId: String,
                      var signInTime: String,
                      var shiftsCode: String,
                      var shiftsCode_Name: String,
                      var diagnosis_Code: String,
                      var user: User):BaseObservable() {

    @SerializedName("statu")
    @get:Bindable
    var status: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.status)
        }
}