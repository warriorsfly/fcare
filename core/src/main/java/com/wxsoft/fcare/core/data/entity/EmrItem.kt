package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

/**
 * 病人emr
 */
data class EmrItem(val id:String,
                   @SerializedName("actionCode")
                   val code: String,
                   val name: String):BaseObservable() {
        @get:Bindable
        @SerializedName("excutedTime")
        var completedAt: String? = null
                get() {
                        return field?.substring(11, 16)
                }
                set(value) {
                        field = value
                        notifyPropertyChanged(BR.completedAt)
                }
        @get:Bindable
        @SerializedName("hasExcuted")
        var done: Boolean = false
                set(value) {
                        field = value
                        notifyPropertyChanged(BR.done)
                }
        @get:Bindable
        @SerializedName("data")
        var result: Any? = null
                set(value) {
                        field = value
                        notifyPropertyChanged(BR.result)
                }
}