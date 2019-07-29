package com.wxsoft.fcare.core.data.entity.rating

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

/**
 * 评分选项
 */
data class Option(val id:String,
                  val subjectId:String,
                  val name:String,
                  val score:Int,
                  @SerializedName("sortNum")
                  val index:Int,
                  @SerializedName("autoCheckOtherOptions")
                  val autoChecks:List<String>):BaseObservable(){
    @Transient
    @get:Bindable
    var checked:Boolean=false
    set(value) {
        field=value
        notifyPropertyChanged(BR.checked)
    }

}