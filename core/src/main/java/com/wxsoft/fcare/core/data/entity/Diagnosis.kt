package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.utils.DateTimeUtils
import java.io.Serializable

data class Diagnosis (var id:String="",val createrId:String,val createrName:String): BaseObservable(), Serializable {

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
            notifyPropertyChanged(BR.diagnosisCode1Name)
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

    @Bindable
    @SerializedName("diagnosisCode3_Name")
    var diagnosisCode3Name: String? = ""
        set(value) {
            field = if (value == "代码不存在") "" else value
            notifyPropertyChanged(BR.diagnosisCode3Name)
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

    @SerializedName("criticalLevel_Name")
    @Bindable
    var criticalLevelName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.criticalLevelName)
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
    var diagnosisTime: String = DateTimeUtils.getCurrentTime()
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosisTime)
        }

    @Bindable
    var handWay: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.handWay)
        }

    @Bindable
    var patientOutcom: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientOutcom)
        }

    @Bindable
    var memo: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.memo)
        }

    @Bindable
    var createdDate:String= DateTimeUtils.getCurrentTime()
        set(value) {
            field=value
            notifyPropertyChanged(BR.createdDate)
        }
    @Bindable
        var sceneType:String= ""
            set(value) {
                field=value
                notifyPropertyChanged(BR.sceneType)
            }


}