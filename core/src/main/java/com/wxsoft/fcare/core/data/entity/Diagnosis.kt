package com.wxsoft.fcare.core.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.fcare.core.BR

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

    /// <summary>
    /// 诊断代码2
    /// </summary>
    @Bindable
    var diagnosisCode2: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosisCode2)
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
}