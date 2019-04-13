package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.data.entity.rating.Rating

data class DiagnoseTreatment (val id:String=""): BaseObservable(){

    //诊断
    @Bindable
    var diagnosis: Diagnosis = Diagnosis("","","")
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosis)
        }

    //策略
    @Bindable
    var treatStrategy :Strategy = Strategy("")
        set(value) {
            field = value
            notifyPropertyChanged(BR.treatStrategy)
        }

    //Grace评分
    @Bindable
    var graceRating :Rating = Rating("","","", emptyList())
        set(value) {
            field = value
            notifyPropertyChanged(BR.graceRating)
        }

    //ACS
    @Bindable
    var acsDrugRecords :List<DrugRecord> = emptyList()
        set(value) {
            field = value
            notifyPropertyChanged(BR.acsDrugRecords)
        }

    //知情谈话
    @Bindable
    var talk :Talk = Talk("")
        set(value) {
            field = value
            notifyPropertyChanged(BR.talk)
        }


}