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

    fun refreshScore(subject: Subject,option: Option){
        subject.check(option)
        score = subjects.sumBy{ it.options.filter { option ->  option.checked }.sumBy { op->op.score } }
    }

    fun refreshScore(){
        score = subjects.sumBy{ it.options.filter { option ->  option.checked }.sumBy { op->op.score } }
    }

}