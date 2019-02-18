package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.utils.DateTimeUtils

data class Diagnosis (val id:String): BaseObservable(){

    /// <summary>
    /// 病人id
    /// </summary>
    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }

    /// <summary>
    /// 诊断代码1
    /// </summary>
    @Bindable
    var diagnosisCode1: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosisCode1)
        }

    @Bindable
    @SerializedName("diagnosisCode1_Name")
    var diagnosisCode1Name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosisCode1)
        }

    /// <summary>
    /// 诊断代码2
    /// </summary>
    @Bindable
    var diagnosisCode2: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosisCode2)
        }

    @Bindable
    @SerializedName("diagnosisCode2_Name")
    var diagnosisCode2Name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosisCode2Name)
        }

    /// <summary>
    /// 诊断代码3
    /// </summary>
    @Bindable
    var diagnosisCode3: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosisCode3)
        }


    /// <summary>
    /// 危重级别
    /// </summary>
    @Bindable
    var criticalLevel: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.criticalLevel)
        }

    @Bindable
    @SerializedName("criticalLevel_Name")
    var criticalLevelName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosisCode1)
        }

    /// <summary>
    /// 诊断地点
    /// </summary>
    @Bindable
    var location: Int = 1
        set(value) {
            field = value
            notifyPropertyChanged(BR.location)
        }

    /// <summary>
    /// 医生
    /// </summary>
    @Bindable
    var doctorId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.doctorId)
        }

    /// <summary>
    /// 医生名称
    /// </summary>
    @Bindable
    var doctorName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.doctorName)
        }

    @Bindable
    var createdDate:String= DateTimeUtils.getCurrentTime()
        set(value) {
            field=value
            notifyPropertyChanged(BR.createdDate)
        }

}