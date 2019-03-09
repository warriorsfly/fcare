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
                  val memo:String="",
                  val resultGrades:List<Grade> = emptyList()

                  ):BaseObservable(){


    var subjects:List<Subject> = emptyList()

    @get:Bindable
    @SerializedName("passScore")var score:Int=0
        set(value) {
            field=value
            notifyPropertyChanged(BR.score)
        }

    @get:Bindable
    @Transient
    var grade: Grade?=null
    set(value) {
        field=value
        notifyPropertyChanged(BR.grade)
    }
    fun refreshScore(subject: Subject,option: Option){
        subject.check(option)
        refreshScore()
    }

    fun refreshScore(){
        score = subjects.sumBy{ it.options.filter { option ->  option.checked }.sumBy { op->op.score } }

        grade=resultGrades.firstOrNull { it.scoreStart<score && it.scoreEnd>= score }
    }

}