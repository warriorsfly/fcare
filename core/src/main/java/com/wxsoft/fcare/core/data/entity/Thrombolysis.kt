package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.utils.DateTimeUtils

class Thrombolysis constructor(@Bindable var id:String="",var createrId:String): BaseObservable(){
    /**
     * 是否溶栓治疗
     * 选填（1 有 0 无，传1,0或空值）
     */
//    @Transient
//    @Bindable
//    var suitable:Boolean=false
//        set(value) {
//            field=value
//            notifyPropertyChanged(BR.suitable)
//            if (value)areSuitable="1" else areSuitable="0"
//            if (value)thrombolysis_Check="1" else thrombolysis_Check="0"
//        }
    /**
     * 是否溶栓治疗
     * 选填（1 有 0 无，传1,0或空值）
     */
    @SerializedName("is_Thrombolysis")
    @Bindable
    var areSuitable:String="1"
        set(value) {
            field=value
            notifyPropertyChanged(BR.areSuitable)
        }

    /**
     *  溶栓治疗代码
     */
    @Bindable
    var thrombolysis_Type:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.thrombolysis_Type)
        }

    /**
     *筛查
     * 选填（1 合适 0 不合适 空值表示未选择，传1,0或空值）
     */
    @Bindable
    var screening:String="1"
        set(value) {
            field=value
            notifyPropertyChanged(BR.screening)
        }

    /**
     * 是否溶栓检查
     * 是否溶栓检查 选填（1 是 2否 空值表示未选择，传1,2或空值）
     */
    @SerializedName("is_Thrombolysis_Check")
    @Bindable
    var thrombolysis_Check:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.thrombolysis_Check)
        }
    /**
     *  是否直达 选填（1 是 2否 空值表示未选择，传1,2或空值）
     */
    @SerializedName("is_Direct")
    @Bindable
    var hasDirect:Boolean=true
        set(value) {
            field=value
            notifyPropertyChanged(BR.hasDirect)
        }

    /**
     *  溶栓场所 字典：16
     */
    @Bindable
    var throm_Treatment_Place:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.throm_Treatment_Place)
        }
    @Bindable
    var thromStaffs:List<ThromboStaff>? = emptyList()
        set(value) {
            field=value
            doctors=field?.joinToString(","){ it.staffName }?:""
            notifyPropertyChanged(BR.thromStaffs)
        }
    @Transient
    @Bindable
    var doctors:String?=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.doctors)
        }

    @Bindable
    @SerializedName("throm_Treatment_Place_Name")
    var thromTreatmentPlaceName:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.thromTreatmentPlaceName)
        }

    /**
     * 开始知情同意
     */
    @Bindable
    var start_Agree_Time:String?=null
        set(value) {
            field=value
            getInformedTime()
            notifyPropertyChanged(BR.start_Agree_Time)
        }

    /**
     *  签署知情同意
     */
    @Bindable
    var sign_Agree_Time:String?=null
        set(value) {
            field=value
            getInformedTime()
            notifyPropertyChanged(BR.sign_Agree_Time)
        }
    /**
     *  知情同意书总计时间
     */
    @Bindable
    @Transient
    var allTime:String?=null
        set(value) {
            field=value
            notifyPropertyChanged(BR.allTime)
        }

    /**
     *  溶栓开始时间
     */
    @Bindable
    var throm_Start_Time:String?=null
        set(value) {
            field=value
            notifyPropertyChanged(BR.throm_Start_Time)
        }
    /**
     *  溶栓结束时间
     */
    @Bindable
    var throm_End_Time:String?=null
        set(value) {
            field=value
            notifyPropertyChanged(BR.throm_End_Time)
        }
    /**
     *  溶栓后造影时间
     */
    @Bindable
    var start_Radiography_Time:String?=null
        set(value) {
            field=value
            notifyPropertyChanged(BR.start_Radiography_Time)
        }
    /**
     * 溶栓药物类别代码  字典：25
     */
//    @Bindable
//    var throm_Drug_Type:String=""
//        set(value) {
//            field=value
//            notifyPropertyChanged(BR.throm_Drug_Type)
//        }

    /**
     *  剂量代码  字典：26
     */
    @Bindable
    var throm_Drug_Code:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.throm_Drug_Code)
        }

    /**
     *溶栓再通
     */
    @SerializedName("is_Repatency")
    @Bindable
    var repatency:Boolean=true
        set(value) {
            field=value
            notifyPropertyChanged(BR.repatency)
        }

    /**
     *结果描述
     */
    @Bindable
    var throm_Result_Desc:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.throm_Result_Desc)
        }

    /**
     *是否院前溶栓
     */
    @SerializedName("is_Pre_Hospital")
    @Bindable
    var pre_Hospital:Boolean=true
        set(value) {
            field=value
            notifyPropertyChanged(BR.pre_Hospital)
        }

    /**
     *  病人Id
     */
    @Bindable
    var patientId:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.patientId)
        }
    /**
     *  并发症
     */
    @Bindable
    var complications:List<Complication>? = emptyList()
        set(value) {
            field=value
            notifyPropertyChanged(BR.complications)
        }

    @Bindable
    @Transient
    var complication:String? = null
        set(value) {
            field=value
            notifyPropertyChanged(BR.complication)
        }

    @Bindable
    var d2N:Int?=0
        set(value) {
            field=value
            notifyPropertyChanged(BR.d2N)
        }

    @Bindable
    var fmC2N:Int?=0
        set(value) {
            field=value
            notifyPropertyChanged(BR.fmC2N)
        }

    /**
     *  用药 第一代 第二代 第三代
     */
    @Bindable
    var throm_Drug_Type_Dt:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.throm_Drug_Type_Dt)
        }
      /**
     *  用药 剂量 全量 半量
     */
    @Bindable
    var throm_Drug_Code_Dt:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.throm_Drug_Code_Dt)
        }


    /**
     *
     */
    @Bindable
    var talkRecordId:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.talkRecordId)
        }

    @Bindable
    var drugRecords:List<DrugRecord> = emptyList()
        set(value){
            field=value
            notifyPropertyChanged(BR.drugRecords)
        }


    @Bindable
    var createdDate:String?=null
        set(value) {
            field=value
            notifyPropertyChanged(BR.createdDate)
        }

    @Bindable
    var direction_Code:String?=null
        set(value) {
            field=value
            notifyPropertyChanged(BR.direction_Code)
        }

    fun getInformedTime(){
        if(start_Agree_Time.isNullOrEmpty()|| sign_Agree_Time.isNullOrEmpty()) return
        allTime = DateTimeUtils.getAAfromBBMinutes(start_Agree_Time!!, sign_Agree_Time!!)

    }

    fun setUpChecked(){
        getInformedTime()
        getComplicationStr()
    }
    fun getComplicationStr(){
        complications?.map {
           if (complication.isNullOrEmpty()){
               complication = it.complicationCode_Name
           }else{
               complication = complication + "、" + it.complicationCode_Name
           }
        }
    }


    fun setPlaceCheck(comefrom:String){
        if (comefrom == "1") {
            throm_Treatment_Place = "16-2"
            thromTreatmentPlaceName = "救护车"
        }else{
            throm_Treatment_Place = "16-3"
            thromTreatmentPlaceName = "本院急诊科"
        }
    }

}