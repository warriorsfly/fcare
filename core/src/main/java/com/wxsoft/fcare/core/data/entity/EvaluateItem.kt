package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class EvaluateItem(var id:String?= null): BaseObservable() {

    var name:String? = null
    @Bindable
    var consciousness_Type_Name: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.consciousness_Type_Name)
        }
    @Bindable
    var consciousness_Type: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.consciousness_Type)
        }
    @Bindable
    var dbp: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.dbp)
        }
    @Bindable
    var sbp: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.sbp)
        }
    @Bindable
    var heart_Rate: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.heart_Rate)
        }
    @Bindable
    var respiration_Rate: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.respiration_Rate)
        }
    @Bindable
    var spO2: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.spO2)
        }
    @Bindable
    var weight: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.weight)
        }
    @Bindable
    var height: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.height)
        }
    @Bindable
    var pulse_Rate: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.pulse_Rate)
        }
    @Bindable
    var nihss: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.nihss)
        }
    var nihsS_AnswerRecordId: String? =  null
    @Bindable
    var mrs: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.mrs)
        }
    var mrS_AnswerRecordId: String? = null
    @Bindable
    var bi: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.bi)
        }
    var bI_AnswerRecordId: String? = null
    @Bindable
    var swallow: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.swallow)
        }

    var swallow_AnswerRecordId: String? = null
    var patientId:String?=null
    var evaluatePlanId:String?=null
    var showColumn:String?= null
    var createdDate:String?= null
    var modifiedDate: String? =null
    var createrId: String? =null
    var createrName: String? =null
    var modifierId: String? =null
    var modifierName: String? =null
    @Transient
    @Bindable
    var columns:List<String> = emptyList()
        set(value) {
            field = value
            notifyPropertyChanged(BR.columns)
        }

    fun selectColumns(){
        columns = showColumn?.split(",")?: emptyList()
    }
}