package com.wxsoft.fcare.core.data.entity.lis

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.Attachment

data class LisCr (val id:String) : BaseObservable() {

    /**
     * 肌钙蛋白：抽血时间
     */
    @Bindable
    @SerializedName("sampling_Time")
    var samplingTime: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.samplingTime)
        }

    /**
     * 报告时间
     */
    @Bindable
    @SerializedName("report_Time")
    var reportTime: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.reportTime)
        }

    /**
     * cTnI
     */
    @Bindable
    @SerializedName("ctni_Value")
    var ctniValue: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ctniValue)
        }

    /**
     * CTNI单位
     * 字典：21
     */
    @Bindable
    @SerializedName("ctni_Unit")
    var ctniUnit: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ctniUnit)
        }
//    @Transient
//    @Bindable
//    var selectCtniUnit:Int=0
//        set(value){
//            field=value
////            ctniUnit = value.toString()
//            notifyPropertyChanged(BR.selectCtniUnit)
//        }

    /**
     * CTNI状态
     * 选填(阴性，阳性或空值)
     */
    @get:Bindable
    @Transient
    var ctniStatus: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ctniStatus)
        }

    /**
     * cTnT
     */
    @Bindable
    @SerializedName("ctnt_Value")
    var ctntValue: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ctntValue)
        }


    /**
     * CTNT单位
     * 字典：21
     */
    @Bindable
    @SerializedName("ctnt_Unit")
    var ctntUnit: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ctntUnit)
        }
//    @Transient
//    @Bindable
//    var selectCtntUnit:Int=0
//        set(value){
//            field=value
//            ctntUnit = value.toString()
//            notifyPropertyChanged(BR.selectCtntUnit)
//        }
    /**
     * CTNT状态
     * 选填(阴性，阳性或空值)
     */
    @Bindable
    @Transient
    var ctntStatus: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ctntStatus)
        }


    /**
     * 血清肌酐（Cr)
     */
    @Bindable
    @SerializedName("cr_Value")
    var crValue: Float = 0.0f
        set(value) {
            field = value
            notifyPropertyChanged(BR.crValue)
        }
    @Bindable
    @Transient
    var crValueStr: String = ""
        set(value) {
            field = value
            if (!value.isNullOrEmpty()) crValue = value.toFloat()
            notifyPropertyChanged(BR.crValueStr)
        }
    /**
     * CR单位
     * 选填（1 umol/L 0 无单位，传1,0或空值）
     */
    @Bindable
    @SerializedName("cr_Unit")
    var crUnit: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.crUnit)
        }
    @Transient
    @Bindable
    var selectCrUnit:Int=0
        set(value){
            field=value
            when(value){
                0->crUnit = "1"
                1->crUnit = "0"
            }
            notifyPropertyChanged(BR.selectCrUnit)
        }

    /**
     * 病人Id
     */
    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    var attachments:List<Attachment>? = emptyList()
        set(value) {
            field = value
            notifyPropertyChanged(BR.attachments)
        }



    @Bindable
    @SerializedName("cK_MB")
    var ckmb: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ckmb)
        }

    @Bindable
    var myo: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.myo)
        }
    @Bindable
    var dDimer: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.dDimer)
        }
    @Bindable
    var bnp: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.bnp)
        }
    @Bindable
    var nTproBNP: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.nTproBNP)
        }


    fun setUpChecked(){
        if(crUnit==null)selectCrUnit = 0
        else {
            when (crUnit) {

                "1" -> selectCrUnit = 0
                "0" -> selectCrUnit = 1
                else -> 0
            }
        }
    
//        if (ctniUnit.isNotEmpty()) selectCtniUnit = ctniUnit.split("-").last().toInt()-1

        crValueStr = if(crValue!=0.0f) crValue.toString() else ""

        if (ctniValue.isNullOrEmpty()||ctniValue.contains("<")){
            ctniStatus = "阴性"
        }else {
            try {
                val dou = ctniValue.toFloat()
                if (dou > 1f) {
                    ctniStatus = "阳性"
                }else {
                    ctniStatus = "阴性"
                }
            } catch (_: Exception){

            }
        }

    }

}