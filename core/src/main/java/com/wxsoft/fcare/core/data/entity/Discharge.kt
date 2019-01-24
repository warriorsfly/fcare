package com.wxsoft.fcare.core.data.entity

import com.google.gson.annotations.SerializedName

data class Discharge(val id:String  =""){
    @SerializedName("outcome_Code")
    var code:String=""
    @SerializedName("outcome_Name")
    var name:String=""
    @SerializedName("treatment_Result_Code")
    var treatCode:String=""
    @SerializedName("treatment_Result_Name")
    var treatName:String=""
    @SerializedName("hand_Time")
    var handleTime:String?=null
    @SerializedName("hand_Hospital_Name")
    var handleHospitalName:String=""
    @SerializedName("death_Time")
    var deathTime:String?=null
    @SerializedName("death_Cause_Code")
    var deathCause:String=""
    @SerializedName("death_Cause_Desc")
    var deathDesc:String=""
    var remark:String=""
    @SerializedName("medical_Desc")
    var medicalDesc:String=""
    @SerializedName("transfer_Date")
    var transferDate:String?=null
    @SerializedName("admission_Dept")
    var admissionDept:String=""
    @SerializedName("transfer_Reason")
    var transferReason:String=""
    @SerializedName("is_Net_Hospital")
    var netHospital:Boolean=false
    @SerializedName("is_Trans_Pci")
    var transPci:String=""
    @SerializedName("decision_Operation_Time")
    var decisionTime:String?=null
    @SerializedName("no_Trans_Pci_Reason")
    var noPciReason:String=""
    @SerializedName("is_Direct_Catheter")
    var directCatheter:Boolean=false
    @SerializedName("out_Grug_Dapt")
    var drugDept:Boolean=false
    @SerializedName("out_Grug_Aceiorarb")
    var drugAceiorarb:Boolean=false
    @SerializedName("out_Drug_Statins")
    var drugStatins:Boolean=false
    @SerializedName("out_Drug_Retardant")
    var drugRetardant:Boolean=false
    var patientId:String=""
    var createdDate:String?=null
    var modifiedDate:String?=null
    var createrId:String?=null
    var createrName:String?=null
    var modifierId:String?=null
    var modifierName:String?=null
}