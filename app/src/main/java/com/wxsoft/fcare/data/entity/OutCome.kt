package com.wxsoft.fcare.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.BR
import com.wxsoft.fcare.R

data class OutCome constructor( var id:String=""): BaseObservable(){

    /**
     *
     */
    @Bindable
    var treatment_Result_Code:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.treatment_Result_Code)
        }
    /**
     *
     */
    @Bindable
    var treatment_Result_Name:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.treatment_Result_Name)
        }

    @Expose
    @Transient
    @Bindable
    var selectedTreatment_ResultIndex:Int= R.id.outcomes_cure
        set(value) {
            field=value

            treatment_Result_Code=when(selectedTreatment_ResultIndex){
                R.id.outcomes_cure->"1"
                R.id.outcomes_improve->"2"
                R.id.outcomes_BD->"3"
                R.id.outcomes_other_reson->"4"
                else->""
            }
            treatment_Result_Name=when(selectedTreatment_ResultIndex){
                R.id.outcomes_cure->"治愈"
                R.id.outcomes_improve->"好转"
                R.id.outcomes_BD->"脑死亡离院"
                R.id.outcomes_other_reson->"其他原因离院"
                else->""
            }
            notifyPropertyChanged(BR.selectedTreatment_ResultIndex)
            notifyPropertyChanged(BR.treatment_Result_Code)
            notifyPropertyChanged(BR.treatment_Result_Name)
        }

    var outcome_Code:String=""
        @Bindable get() {return field}
    var outcome_Name:String=""
        @Bindable get() {return field}

    @Expose
    @Transient
    @Bindable
    var selectedOutcomeIndex:Int= R.id.is_out
        set(value) {
            field=value

            outcome_Code=when(selectedOutcomeIndex){
                R.id.is_out->"1"
                R.id.is_toOtherHospital->"2"
                R.id.is_toOtherDepartment->"3"
                R.id.is_die->"4"
                else->""
            }
            outcome_Name=when(selectedOutcomeIndex){
                R.id.is_out->"出院"
                R.id.is_toOtherHospital->"转送其他医院"
                R.id.is_toOtherDepartment->"转送其他科室"
                R.id.is_die->"死亡"
                else->""
            }
            notifyPropertyChanged(BR.selectedOutcomeIndex)
            notifyPropertyChanged(BR.outcome_Code)
            notifyPropertyChanged(BR.outcome_Name)
        }


    /**
     *
     */
    @Bindable
    var hand_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.hand_Time)
        }
    /**
     *
     */
    @Bindable
    var hand_Hospital_Name:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.hand_Hospital_Name)
        }
    /**
     *
     */
    @Bindable
    var decision_Operation_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.decision_Operation_Time)
        }
    /**
     *
     */
    @Bindable
    var death_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.death_Time)
        }
    /**
     *
     */
    @Bindable
    var death_Cause_Code:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.death_Cause_Code)
        }
    /**
     *
     */
    @Bindable
    var death_Cause_Desc:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.death_Cause_Desc)
        }
    @Transient
    @Bindable
    var select_death_Cause:Boolean=false
        set(value) {
            field=value
            if(value){
                death_Cause_Code = "1"
                death_Cause_Desc = "心源性"
            }else {
                death_Cause_Code = "2"
                death_Cause_Desc = "非心源性"
            }
            notifyPropertyChanged(BR.select_death_Cause)
        }
    /**
     *
     */
    @Bindable
    var remark:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.remark)
        }
    /**
     *
     */
    @Bindable
    var medical_Desc:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.medical_Desc)
        }
    /**
     *
     */
    @Bindable
    var transfer_Date:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.transfer_Date)
        }
    /**
     *
     */
    @Bindable
    var admission_Dept:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.admission_Dept)
        }
    /**
     *
     */
    @Bindable
    var transfer_Reason:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.transfer_Reason)
        }
    /**
     *
     */
    @SerializedName("is_Net_Hospital")
    @Bindable
    var has_Net_Hospital:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.has_Net_Hospital)
        }
    /**
     *
     */
    @SerializedName("is_Trans_Pci")
    @Bindable
    var has_Trans_Pci:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.has_Trans_Pci)
        }

    @Bindable
    var select_Trans_Pci:Boolean=false
        set(value) {
            field=value
            if(value) has_Trans_Pci="1" else has_Trans_Pci="2"
            notifyPropertyChanged(BR.select_Trans_Pci)
        }

    /**
     *
     */
    @Bindable
    var no_Trans_Pci_Reason:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.no_Trans_Pci_Reason)
        }
    /**
     *
     */
    @SerializedName("is_Direct_Catheter")
    @Bindable
    var has_Direct_Catheter:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.has_Direct_Catheter)
        }
    @Bindable
    var select_Direct_Catheter:Boolean=false
        set(value) {
            field=value
            if(value) has_Direct_Catheter="1" else has_Direct_Catheter="2"
            notifyPropertyChanged(BR.select_Direct_Catheter)
        }
    /**
     *
     */
    @Bindable
    var out_Grug_Dapt:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.out_Grug_Dapt)
        }
    /**
     *
     */
    @Bindable
    var out_Grug_Aceiorarb:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.out_Grug_Aceiorarb)
        }
    /**
     *
     */
    @Bindable
    var out_Drug_Statins:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.out_Drug_Statins)
        }
    /**
     *
     */
    @Bindable
    var out_Drug_Retardant:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.out_Drug_Retardant)
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





    fun setUpChecked(){
        select_Trans_Pci = has_Trans_Pci.equals("1")
        select_Direct_Catheter = has_Direct_Catheter.equals("1")
        selectedOutcomeIndex =when(outcome_Code){
            "1"-> R.id.is_out
            "2"-> R.id.is_toOtherHospital
            "3"-> R.id.is_toOtherDepartment
            "4"-> R.id.is_die
            else->R.id.is_out
        }
        selectedTreatment_ResultIndex = when(treatment_Result_Code){
            "1"-> R.id.outcomes_cure
            "2"-> R.id.outcomes_improve
            "3"-> R.id.outcomes_BD
            "4"-> R.id.outcomes_other_reson
            else->R.id.outcomes_cure
        }

    }




}