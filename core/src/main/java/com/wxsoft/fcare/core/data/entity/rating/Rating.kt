package com.wxsoft.fcare.core.data.entity.rating

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

/**
 * 评分
 */
data class Rating(val id:String="",
                  val name:String="",
                  val memo:String=""
                  ):BaseObservable(){


    var subjects:List<Subject> = emptyList()

    @get:Bindable
    @SerializedName("passScore")var score:Int=0
        set(value) {
            field=value
            notifyPropertyChanged(BR.score)
        }

    fun changeScore(){
        score = subjects.sumBy{ it.options.sumBy { op->op.score } }
    }

}