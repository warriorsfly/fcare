package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class DisChargeDiagnosis (val id:String=""): BaseObservable(){

    /// <summary>
    /// 病人id
    /// </summary>
    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    var diagnosis: Diagnosis = Diagnosis("","","")
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosis)
        }

    /// <summary>
    /// 诊断名称
    /// </summary>
    @Transient
    @Bindable
    var diagnosisName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosisName)
        }


    /// <summary>
    /// 诊断时间
    /// </summary>
    @Bindable
    @SerializedName("diagnosis_Time")
    var diagnosisTime: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosisTime)
        }

    @Bindable
    @SerializedName("his_Bed_Doctor")
    var hisBedDoctor: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.hisBedDoctor)
        }

    /// <summary>
    /// 心衰
    /// </summary>
    @Bindable
    @SerializedName("is_Heart_Failure")
    var heartFailure: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.heartFailure)
        }

    /// <summary>
    /// 住院天数
    /// </summary>
    @Bindable
    @SerializedName("hod")
    var days: Float? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.days)
        }
    @Transient
    @Bindable
    var daysStr: String = ""
        set(value) {
            field = value
            days = value.toFloatOrNull()
            notifyPropertyChanged(BR.daysStr)
        }

    /// <summary>
    /// 总费用
    /// </summary>
    @Bindable
    @SerializedName("total_Cost")
    var cost: Float? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.cost)
        }
    @Transient
    @Bindable
    var costStr: String = ""
        set(value) {
            field = value
            cost = value.toFloatOrNull()
            notifyPropertyChanged(BR.costStr)
        }

    //入院日期
    @Bindable
    @SerializedName("in_Time")
    var inTime: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.inTime)
        }
    //出院日期
    @Bindable
    @SerializedName("leave_Time")
    var leaveTime: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.leaveTime)
        }

    //HIS出院诊断名称
    @Bindable
    @SerializedName("his_Diagnosis_Name")
    var hisDiagnosisName: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.hisDiagnosisName)
        }

    //HIS治疗结果
    @Bindable
    @SerializedName("his_Treatment_Result")
    var hisTreatmentResult: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.hisTreatmentResult)
        }


    @Bindable
    var createdDate:String?=null
        set(value) {
            field=value
            notifyPropertyChanged(BR.createdDate)
        }
    var createrId:String=""
    var createrName:String=""

    fun haveLoaded(){
        if (days != null) daysStr = days.toString()
        if (cost != null) costStr = cost.toString()
        if (diagnosis != null){
            if (diagnosis.diagnosisCode3Name.equals(other = "代码不存在")){
                diagnosis.diagnosisCode3Name = ""
            }
            if (diagnosis.diagnosisCode2Name.isNullOrEmpty()) diagnosis.diagnosisCode2Name = ""
            if (diagnosis.diagnosisCode3Name.isNullOrEmpty()) diagnosis.diagnosisCode3Name = ""
        }else{
            diagnosis = Diagnosis("","","")
        }
    }
}