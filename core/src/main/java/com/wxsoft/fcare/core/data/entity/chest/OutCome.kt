package com.wxsoft.fcare.core.data.entity.chest

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class OutCome(val id:String="", val patientId: String, val createrId: String):BaseObservable() {

    @get:Bindable
    @SerializedName("outcome_Code")
    var outcomeCode: String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.outcomeCode)
        }
    @SerializedName("outcome_Code_Name")
    var outcomeName: String = ""
    @Bindable
    @SerializedName("treatment_Result_Code")
    var resultCode: String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.resultCode)
        }
    @SerializedName("treatment_Result_Code_Name")
    var resultName: String = ""
    @Bindable
    @SerializedName("hand_Time")
    var handTime: String? = null
        set(value) {
            field=value
            notifyPropertyChanged(BR.handTime)
        }
    @Bindable
    @SerializedName("hand_Hospital_Name")
    var hospitalName: String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.hospitalName)
        }
    @Bindable
    @SerializedName("death_Time")
    var deadAt: String? = null
        set(value) {
            field=value
            notifyPropertyChanged(BR.deadAt)
        }
    @Bindable
    @SerializedName("death_Cause_Code")
    var deathCauseBy: Boolean = false
        set(value) {
            field=value
            notifyPropertyChanged(BR.deathCauseBy)
        }
    @Bindable
    @SerializedName("death_Cause_Desc")
    var deathDesc: String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.deathDesc)
        }
    @Bindable
    var remark: String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.remark)
        }
    @Bindable
    @SerializedName("medical_Desc")
    var medicalDesc: String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.medicalDesc)
        }
    @Bindable
    @SerializedName("transfer_Date")
    var transferAt: String? = null
        set(value) {
            field=value
            notifyPropertyChanged(BR.transferAt)
        }
    @Bindable
    @SerializedName("admission_Dept")
    var admissionDept: String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.admissionDept)
        }
    @Bindable
    @SerializedName("transfer_Reason")
    var transferFor: String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.transferFor)
        }
    @Bindable
    @SerializedName("is_Net_Hospital")
    var net: Boolean = false
        set(value) {
            field=value
            notifyPropertyChanged(BR.net)
        }
    @Bindable
    @SerializedName("is_Trans_Pci")
    var pci: Boolean = false
        set(value) {
            field=value
            notifyPropertyChanged(BR.pci)
        }
    @Bindable
    @SerializedName("decision_Operation_Time")
    var operationTime: String? = null
        set(value) {
            field=value
            notifyPropertyChanged(BR.operationTime)
        }
    @Bindable
    @SerializedName("no_Trans_Pci_Reason")
    var noPciReason: String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.noPciReason)
        }
    @Bindable
    @SerializedName("is_Direct_Catheter")
    var cathetered: Boolean = false
        set(value) {
            field=value
            notifyPropertyChanged(BR.cathetered)
        }
    @Bindable
    @SerializedName("out_Grug_Dapt")
    var outDapt: Boolean = false
        set(value) {
            field=value
            notifyPropertyChanged(BR.outDapt)
        }
    @Bindable
    @SerializedName("out_Grug_Aceiorarb")
    var outAceiorarb: Boolean = false
        set(value) {
            field=value
            notifyPropertyChanged(BR.outAceiorarb)
        }
    @Bindable
    @SerializedName("out_Drug_Statins")
    var outStatins: Boolean = false
        set(value) {
            field=value
            notifyPropertyChanged(BR.outStatins)
        }
    @Bindable
    @SerializedName("out_Drug_Retardant")
    var outRetardant: Boolean = false
        set(value) {
            field=value
            notifyPropertyChanged(BR.outRetardant)
        }

    var createdDate: String? = null
    var modifiedDate: String? = null

    var createrName: String = ""
    var modifierId: String = ""
    var modifierName: String = ""

    fun setCode(index:Int){
        outcomeCode=when(index){
            0->"11-1"
            1->"11-2"
            2->"11-3"
            3->"11-4"
            else->throw IllegalStateException("Unknown index $index")
        }

    }

    fun setResultCode(index:Int){
        resultCode=when(index){
            0->"12-1"
            1->"12-2"
            2->"12-3"
            3->"12-4"
            else->throw IllegalStateException("Unknown index $index")
        }

    }
}
