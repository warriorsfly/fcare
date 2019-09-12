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
    @get:Bindable
    var features=""

    @Transient
    @get:Bindable
    var savable=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.savable)
        }

    // 接收120/网络医院心电图
    @Bindable
    var net_Transto_Basic:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.net_Transto_Basic)
        }
    // 接收120/网络医院心电图时间
    @Bindable
    var tran_Date: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.tran_Date)
        }
    // 接收120/网络医院 传输方式 (1.实时监控,2.微信群,3.短信)
    @Bindable
    var is_Remote_Ecgtran: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.is_Remote_Ecgtran)
        }
    // 传输心电图至协作医院
    @Bindable
    var basic_Transto_Standard:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.basic_Transto_Standard)
        }
    // 传输心电图至协作医院时间
    @Bindable
    var remote_Ecg_Tran_Date : String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.remote_Ecg_Tran_Date)
        }
    // 协作医院 传输方式 (1.实时监控,2.微信群,3.短信)
    @Bindable
    var remote_Ecgtran_Way: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.remote_Ecgtran_Way)
        }
    // 两者都无
    @Bindable
    var not_Trans:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.not_Trans)
        }

    @get:Bindable
    @SerializedName("electroCardiogramDiagnoses")
    var diagnoses:List<EcgDiagnose> = emptyList()
        set(value){
            field=value
            diagnoseText=field.joinToString("\n",transform={ (diagnoses.indexOf(it)+1).toString()+"."+it.name})
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
    var attachments:List<Attachment>?  = emptyList()
}