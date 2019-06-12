package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.drug.ACSDrug
import com.wxsoft.fcare.core.data.entity.rating.RatingRecord

data class DiagnoseTreatment (val id:String="" ,var createrId:String? = null,
                        var createrName:String? = null): BaseObservable(){

    //诊断
    @Bindable
    var diagnosis: Diagnosis = Diagnosis("","","")
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosis)
        }

    //策略
    @Bindable
    var treatStrategy :Strategy = Strategy("",1)
        set(value) {
            field = value
            notifyPropertyChanged(BR.treatStrategy)
        }

    //Grace评分
    @Bindable
    var graceRating : RatingRecord = RatingRecord("","","","","","","")
        set(value) {
            field = value
            notifyPropertyChanged(BR.graceRating)
        }

    //ACS
    @Bindable
    var acs :ACSDrug = ACSDrug("",createrId,createrName)
        set(value) {
            field = value
            notifyPropertyChanged(BR.acs)
        }

    //知情谈话
    @Bindable
    var talk :Talk = Talk("")
        set(value) {
            field = value
            notifyPropertyChanged(BR.talk)
        }


}