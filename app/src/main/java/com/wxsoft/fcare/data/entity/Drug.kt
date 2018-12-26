package com.wxsoft.fcare.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.BR
import com.wxsoft.fcare.utils.TimesUtils
import java.util.*

data class Drug  constructor( var id:String=""): BaseObservable() {

    /**
     * 是否首次抗凝给药
     */
    @Transient
    @Bindable var areAnticoagulation:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.areAnticoagulation)
            if (value)anticoagulation="1" else anticoagulation="2"
        }

    /**
     * 是否首次抗凝给药
     */
    @SerializedName("is_Anticoagulation")
    @Bindable var anticoagulation:String="2"
        set(value) {
            field=value
            notifyPropertyChanged(BR.anticoagulation)
        }
    /**
     * 是氯吡格雷或者是替格瑞洛
     */
    @Bindable var acs_Drug_Type:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.acs_Drug_Type)
        }
    /**
     * 是氯吡格雷 否则是替格瑞洛
     */
    @Transient
    @Bindable var clopidogrel:Boolean= acs_Drug_Type.equals("1")
        set(value) {
            field=value
            notifyPropertyChanged(BR.clopidogrel)
            if (value)acs_Drug_Type="1" else acs_Drug_Type="2"
        }

    /**
     * 24小时强化他汀治疗 1是 2否
     */
    @Bindable var intensify_Statins_Treat:String="2"
        set(value) {
            field=value
            notifyPropertyChanged(BR.intensify_Statins_Treat)
        }

    /**
     * 24小时强化他汀治疗 1是 2否
     */
    @Transient
    @Bindable var intensifyTreat:Boolean= intensify_Statins_Treat.equals("1")
        set(value) {
            field=value
            notifyPropertyChanged(BR.intensifyTreat)
            if (value)intensify_Statins_Treat="1" else intensify_Statins_Treat="2"
        }

    /**
     * β受体阻滞剂使用
     */
    @Bindable var receptor_Retardant_Using:String="2"
        set(value) {
            field=value
            notifyPropertyChanged(BR.receptor_Retardant_Using)
        }
    /**
     * β受体阻滞剂使用
     */
    @Transient
    @Bindable var receptorUsing:Boolean= receptor_Retardant_Using.equals("1")
        set(value) {
            field=value
            notifyPropertyChanged(BR.receptorUsing)
            if (value)receptor_Retardant_Using="1" else receptor_Retardant_Using="2"
        }


    /**
     * 首次抗凝给药时间
     */
    @Bindable var anticoagulation_Date:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.anticoagulation_Date)
        }

    /**
     * 首次抗血小板给药时间
     */
    @Bindable var acs_Delivery_Time:String=""
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
    @Bindable var anticoagulation_Drug_Name:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.anticoagulation_Drug_Name)
        }


    /**
     * 首次抗凝给药药物
     */
    @Bindable var anticoagulation_Drug:String=""
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



    fun setUpChecked(){
        areAnticoagulation = anticoagulation.equals("1")
        intensifyTreat = intensify_Statins_Treat.equals("1")
        receptorUsing = receptor_Retardant_Using.equals("1")

    }


}