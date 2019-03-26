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
    var diagnosedAt:String?=null
    /**
     * 心电图时间
     */
    @get:Bindable
    @SerializedName("ecgTime")
    var time:String?=null
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
    @SerializedName("diagnoseDoctorName")
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
    @SerializedName("electroCardiogramDiagnoses")
    var diagnoses:List<EcgDiagnose> = emptyList()
    set(value){
      field=value
      diagnoseText=field.joinToString("\n",transform={ (field.indexOf(it)+1).toString()+"."+it.name})
    }

    @Transient
    @get:Bindable
    var diagnoseText:String=""

    @get:Bindable
    var location = 2
    @get:Bindable
    var fmC2ECG = 0f
    @get:Bindable
    var ecg = 0f
    @get:Bindable
    var attachments:List<Attachment>  = emptyList()
}