package com.wxsoft.fcare.core.data.entity.rating

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

/**
 * 评分题
 */
data class Subject(val id:String,
                   @SerializedName("scoreSheetId")val ratingId:String,
                   val name:String,
                   @SerializedName("sortNum")val index:Int):BaseObservable(){
    @SerializedName("subjectOptions")
    var options:List<Option> =  emptyList()

    @Transient
    @get:Bindable
    var selectedIndex:Int?=-1
    set(value) {
        field=value
        notifyPropertyChanged(BR.selectedIndex)
    }

    fun check(option: Option){
        selectedIndex = if(selectedIndex==-1){
            options.indexOf(option)
        }else{
            if(options.indexOf(option)==selectedIndex) {
                -1

            }else{
                options.indexOf(option)
            }
        }
    }
}