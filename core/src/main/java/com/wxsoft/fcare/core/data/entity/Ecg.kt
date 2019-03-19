package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class Ecg(val id:String="",val createrId:String):BaseObservable(){
    var patientId=""

    /**
     * 确诊时间
     */
    @SerializedName("ecgDiagnoseTime")
    @get:Bindable
    var diagnosedAt="0001-01-01 00:00:00"
    /**
     * 心电图时间
     */
    @get:Bindable
    @SerializedName("ecgTime")
    var time="0001-01-01 00:00:00"
    /**
     * 是否远程传输
     */
    @SerializedName("isRemoteEcgtranChecked")
    @get:Bindable
    var remote=false
    /**
     * 是否已判读
     */
    @get:Bindable
    @SerializedName("isDiagnosed")
    var diagnosed=false

    @get:Bindable
    @SerializedName("diagnoseDoctorId")
    var doctorId=""

    @get:Bindable
    @SerializedName("diagnoseDocgorName")
    var doctorName=""
    @get:Bindable
    var diagnoseResult=""

    @Transient
    @get:Bindable
    var savable=false
    set(value) {
        field=value
        notifyPropertyChanged(BR.savable)
    }

    @get:Bindable
    var location = 2
    @get:Bindable
    var fmC2ECG = 0f
    @get:Bindable
    var ecg = 0f
    @get:Bindable
    var attachments:List<Attachment>  = emptyList()
}