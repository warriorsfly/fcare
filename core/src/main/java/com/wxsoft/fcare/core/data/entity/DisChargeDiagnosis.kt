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
    @SerializedName("cp_Diagnosis_Code")
    var diagnosisCode: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosisCode)
        }

    /// <summary>
    /// 诊断名称
    /// </summary>
    @Bindable
    @SerializedName("cp_Diagnosis_Name")
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

    /// <summary>
    /// 心衰
    /// </summary>
    @Bindable
    @SerializedName("is_Heart_Failure")
    var heartFailure: String? = null
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

    /// <summary>
    /// 总费用
    /// </summary>
    @Bindable
    @SerializedName("leave_Time")
    var leave: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.leave)
        }


    @Bindable
    var createdDate:String="2019-01-19 12:00:00"
        set(value) {
            field=value
            notifyPropertyChanged(BR.createdDate)
        }
    var createrId:String=""
    var createrName:String=""
}