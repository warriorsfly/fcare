package com.wxsoft.fcare.core.data.entity.rating

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

/**
 * 评分
 */
data class Rating(val id:String,
                  val name:String,
                  val memo:String
                  ):BaseObservable(){


    var subjects:List<Subject> = emptyList()

    @get:Bindable
    @SerializedName("passScore")var score:Float=0f
        set(value) {
            field=value
            notifyPropertyChanged(BR.score)
        }

    fun changeScore(){
        score = subjects.filter { (it.selectedIndex?:-1)>=0 }
            .sumByDouble{ it.options[it.selectedIndex!!].score.toDouble() }.toFloat()
    }

}