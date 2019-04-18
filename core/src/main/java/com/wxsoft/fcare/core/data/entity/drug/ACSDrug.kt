package com.wxsoft.fcare.core.data.entity.drug

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.utils.DateTimeUtils
import java.io.Serializable

data class ACSDrug constructor( var id:String=""): BaseObservable() , Serializable {

    @Transient
    @Bindable var haveData:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.haveData)
        }

    /**
     * 是否首次抗凝给药
     */
    @SerializedName("is_Anticoagulation")
    @Bindable var anticoagulation:Boolean=true
        set(value) {
            field=value
            notifyPropertyChanged(BR.anticoagulation)
        }
    /**
     * 是氯吡格雷或者是替格瑞洛
     */
    @Bindable var acs_Drug_Type:String="235-1"
        set(value) {
            field=value
            notifyPropertyChanged(BR.acs_Drug_Type)
        }
    /**
     * 是氯吡格雷或者是替格瑞洛
     */
    @Bindable var acsDrugTypeName:String="替格瑞洛"
        set(value) {
            field=value
            notifyPropertyChanged(BR.acsDrugTypeName)
        }

    /**
     * 是氯吡格雷 否则是替格瑞洛
     */
    @Transient
    @Bindable var clopidogrel:Boolean=if(acs_Drug_Type.equals("1")) true else false
        set(value) {
            field=value
            notifyPropertyChanged(BR.clopidogrel)
            if (value)acs_Drug_Type="1" else acs_Drug_Type="2"
        }

    /**
     * 首次抗凝给药时间
     */
    @Bindable var anticoagulation_Date:String= DateTimeUtils.getCurrentTime()
        set(value) {
            field=value
            notifyPropertyChanged(BR.anticoagulation_Date)
        }

    /**
     * 首次抗血小板给药时间
     */
    @Bindable var acs_Delivery_Time:String=DateTimeUtils.getCurrentTime()
        set(value) {
            field=value
            notifyPropertyChanged(BR.acs_Delivery_Time)
        }

    /**
     * 阿司匹林用量
     */
    @Bindable var aspirin_Dose:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.aspirin_Dose)
        }

    /**
     * 氯吡格雷 或者 替格瑞洛 用量
     */
    @Bindable var acs_Drug_Dose:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.acs_Drug_Dose)
        }

    /**
     * 首次抗凝给药药物
     */
    @Bindable var anticoagulation_Drug_Name:String="普通肝素"
        set(value) {
            field=value
            notifyPropertyChanged(BR.anticoagulation_Drug_Name)
        }


    /**
     * 首次抗凝给药药物
     */
    @Bindable var anticoagulation_Drug:String="22-1"
        set(value) {
            field=value
            notifyPropertyChanged(BR.anticoagulation_Drug)
        }

    /**
     * 首次抗凝给药数量
     */
    @Bindable var anticoagulation_Unit:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.anticoagulation_Unit)
        }

    /**
     * 患者id
     */
    @Bindable var patientId:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.patientId)
        }


    @Transient
    @Bindable var drugsStr:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.drugsStr)
        }

    fun haveDrugs(){
        drugsStr =  "阿司匹林"+aspirin_Dose+"mg" + (if (acs_Drug_Dose.isNullOrEmpty()) ""  else acsDrugTypeName +acs_Drug_Dose+"mg")
        if (aspirin_Dose.isNullOrEmpty()) haveData = false else haveData = true
    }

}
