package com.wxsoft.fcare.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.BR

class Thrombolysis constructor(@Bindable var id:String=""): BaseObservable(){
    /**
     * 是否溶栓治疗
     * 选填（1 有 0 无，传1,0或空值）
     */
    @Transient
    @Bindable var suitable:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.suitable)
            if (value)areSuitable="1" else areSuitable="0"
            if (value)thrombolysis_Check="1" else thrombolysis_Check="0"
        }
    /**
     * 是否溶栓治疗
     * 选填（1 有 0 无，传1,0或空值）
     */
    @SerializedName("is_Thrombolysis")
    @Bindable var areSuitable:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.areSuitable)
        }

    /**
     *  溶栓治疗代码
     */
    @Bindable var thrombolysis_Type:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.thrombolysis_Type)
        }

    /**
     *
     */
    @Transient
    @Bindable var hasScreening:Boolean = false
        set(value) {
            field=value
            notifyPropertyChanged(BR.hasScreening)
            if (value)screening="1" else screening="2"
        }

    /**
     *筛查
     * 选填（1 合适 0 不合适 空值表示未选择，传1,0或空值）
     */
    @Bindable var screening:String="0"
        set(value) {
            field=value
            notifyPropertyChanged(BR.screening)
        }

    /**
     * 是否溶栓检查
     */
    @SerializedName("is_Thrombolysis_Check")
    @Bindable var thrombolysis_Check:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.thrombolysis_Check)
        }
    /**
     *  是否直达 选填（1 是 2否 空值表示未选择，传1,2或空值）
     */
    @SerializedName("is_Direct")
    @Bindable var direct:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.direct)
        }

    /**
     *  是否直达 选填（1 是 2否 空值表示未选择，传1,2或空值）
     */
    @Transient
    @Bindable var hasDirect:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.hasDirect)
            if (value)direct="1" else direct="2"

        }

    /**
     *  溶栓场所 字典：16
     */
    @Bindable var throm_Treatment_Place:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.throm_Treatment_Place)
        }
    /**
     * 开始知情同意
     */
    @Bindable var start_Agree_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.start_Agree_Time)
        }

    /**
     *  签署知情同意
     */
    @Bindable var sign_Agree_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.sign_Agree_Time)
        }

    /**
     *  溶栓开始时间
     */
    @Bindable var throm_Start_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.throm_Start_Time)
        }
    /**
     *  溶栓结束时间
     */
    @Bindable var throm_End_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.throm_End_Time)
        }
    /**
     *  溶栓后造影时间
     */
    @Bindable var start_Radiography_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.start_Radiography_Time)
        }
    /**
     * 溶栓药物类别代码  字典：25
     */
    @Bindable var throm_Drug_Type:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.throm_Drug_Type)
        }

    /**
     *  剂量代码  字典：26
     */
    @Bindable var throm_Drug_Code:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.throm_Drug_Code)
        }

    /**
     *溶栓再通
     */
    @SerializedName("is_Repatency")
    @Bindable var repatency:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.repatency)
        }

    /**
     *溶栓再通
     */
    @Transient
    @Bindable var hasRepatency:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.hasRepatency)
            if (value)repatency="1" else repatency="2"
        }

    /**
     *结果描述
     */
    @Bindable var throm_Result_Desc:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.throm_Result_Desc)
        }

    /**
     *是否院前溶栓
     */
    @SerializedName("is_Pre_Hospital")
    @Bindable var pre_Hospital:Boolean=true
        set(value) {
            field=value
            notifyPropertyChanged(BR.pre_Hospital)
        }

    /**
     *  病人Id
     */
    @Bindable var patientId:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.patientId)
        }



    fun setUpChecked(){
        suitable = areSuitable.equals("1")
        hasScreening = screening.equals("1")
        hasDirect = direct.equals("1")
        hasRepatency = repatency.equals("1")


    }

}