package com.wxsoft.fcare.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.BR
import com.wxsoft.fcare.R

data class OutHospitalDiagnosis constructor( var id:String=""): BaseObservable(){

    /**
     *
     */
    var cp_Diagnosis_Code:String=""
        @Bindable get() {return field}
    /**
     *
     */
    var cp_Diagnosis_Name:String=""
        @Bindable get() {return field}

    @Expose
    @Transient
    @Bindable
    var selectedDiagnosis:Int= R.id.is_STEMI
        set(value) {
            field=value

            cp_Diagnosis_Code=when(selectedDiagnosis){
                R.id.is_STEMI->"2"
                R.id.is_NSTEMI->"3"
                R.id.is_UA->"4"
                R.id.is_AD->"5"
                R.id.is_PE->"6"
                R.id.not_ACS->"7"
                R.id.other->"10"
                else->""
            }
            cp_Diagnosis_Name=when(selectedDiagnosis){
                R.id.is_STEMI->"STEMI"
                R.id.is_NSTEMI->"NSTEMI"
                R.id.is_UA->"UA"
                R.id.is_AD->"主动脉夹层"
                R.id.is_PE->"肺动脉栓塞"
                R.id.not_ACS->"非ACS心源性胸痛"
                R.id.other->"其他"
                else->""
            }
            notifyPropertyChanged(BR.selectedOutcomeIndex)
            notifyPropertyChanged(BR.cp_Diagnosis_Code)
            notifyPropertyChanged(BR.cp_Diagnosis_Name)
        }




    /**
     *
     */
    @Bindable
    var diagnosis_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.diagnosis_Time)
        }

    /**
     *
     */
    @SerializedName("is_Heart_Failure")
    @Bindable
    var has_Heart_Failure:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.has_Heart_Failure)
        }

    /**
     *
     */
    @Bindable
    var hod:Int=0
        set(value) {
            field=value
            notifyPropertyChanged(BR.hod)
        }
    /**
     *
     */
    @Bindable
    var total_Cost:Float=0f
        set(value) {
            field=value
            notifyPropertyChanged(BR.total_Cost)
        }
    @Transient
    @Bindable
    var total_Cost_text:String=""
        set(value) {
            field=value
            if(value.isNotEmpty())total_Cost = value.toFloat()
            notifyPropertyChanged(BR.total_Cost_text)
        }

    /**
     *
     */
    @Bindable
    var patientId:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.patientId)
        }

    /**
     *
     */
    @Bindable
    var createrId:String="admin"
        set(value) {
            field=value
            notifyPropertyChanged(BR.createrId)
        }


    fun setUpChecked(){
        if (!total_Cost.equals(0f))total_Cost_text = total_Cost.toString()
        selectedDiagnosis = when(cp_Diagnosis_Code){
            "2"-> R.id.is_STEMI
            "3"-> R.id.is_NSTEMI
            "4"-> R.id.is_UA
            "5"-> R.id.is_AD
            "6"-> R.id.is_PE
            "7"-> R.id.not_ACS
            "10"-> R.id.other
            else->R.id.outcomes_cure
        }
    }

}