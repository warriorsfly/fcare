package com.wxsoft.fcare.data.entity


import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.BR
import com.wxsoft.fcare.data.entity.chest.Evaluation
import com.wxsoft.fcare.data.entity.chest.VitalSign

/**
 * 病人信息
 */
@Entity(tableName = "patients")
data class Patient constructor(@PrimaryKey var id:String): BaseObservable() {


    @Bindable var name: String  =""
    set(value) {
        field=value
        notifyPropertyChanged(BR.name)
    }

    /**
     * 年龄
     */
    @Bindable var age_Value:String="1"
        set(value) {
            field=value
            notifyPropertyChanged(BR.age_Value)
        }

    /**
     * 年龄单位
     */
    @Bindable var age_Unit:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.age_Unit)
        }

    /**
     * 性别 1男2女
     */
     var gender:Int=1


    @Bindable
    @Expose
    @Transient
    @Ignore
    var man:Boolean=true
    set(value) {
        field=value
        gender=if(field)1 else 2
        notifyPropertyChanged(BR.man)
    }
    /**
     * 医院id
     */
    @Bindable var hospital_Id:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.hospital_Id)
        }

    /**
     * 身份证号
     */
    @Bindable var idcard:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.idcard)
        }

    /**
     * 联系电话
     */
    @Bindable var contact_Phone:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.contact_Phone)
        }

    /**
     * 门诊id
     */
    @Bindable var outpatient_Id:String?=null
        set(value) {
            field=value
            notifyPropertyChanged(BR.outpatient_Id)
        }
    /**
     * 住院id
     */
    @Bindable var inpatient_Id:String?=null
        set(value) {

            field=value
            notifyPropertyChanged(BR.inpatient_Id)
        }
    /**
     * 发病地址
     */
    @Bindable var attack_Address:String?=null
        set(value) {

            field=value
            notifyPropertyChanged(BR.attack_Address)
        }

    /**
     * 腕带
     */
    @Bindable var wristband_Number:String?=null
        set(value) {

            field=value
            notifyPropertyChanged(BR.wristband_Number)
        }
    /**
     * 发病时间
     */
    @Bindable var attack_Time:String?=null
        set(value) {

            field=value
            notifyPropertyChanged(BR.attack_Time)
        }
    /**
     * 发病时间无法精确到分 1是0否
     */
    @Ignore
    @Bindable var is_Null_Attack_Detail_Time:Boolean =true
        set(value) {
            field=value
            notifyPropertyChanged(BR.is_Null_Attack_Detail_Time)
        }
    /**
     * 创建账户
     */
    @Bindable var createrId:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.createrId)
        }
    /**
     * 创建人姓名
     */
    @Bindable var createrName:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.createrName)
        }
    /**
     * 创建时间
     */
//    @Transient
//    @Ignore

    @Expose(deserialize = true,serialize = false)
    @Bindable var createdDate:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.createdDate)
        }    /**
     * 更新者姓名
     */
    @Bindable var modifierName:String?=null
        set(value) {

            field=value
            notifyPropertyChanged(BR.modifierName)
        }
    /**
     * 更新时间
     */
    @Bindable var modifiedDate:String?=null
        set(value) {

            field=value
            notifyPropertyChanged(BR.modifiedDate)
        }
    /**
     * 登记时间
     */
    @Ignore
    @Bindable var register_Date:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.register_Date)
        }
    /**
     * 呼救时间
     */
    @Bindable var help_Date:String?=null
        set(value) {

            field=value
            notifyPropertyChanged(BR.help_Date)
        }

    /**
     * 呼救来源
     */

    @SerializedName("is_Help")
    @Bindable var helped:Boolean=false
        set(value) {

            field=value
            notifyPropertyChanged(BR.helped)
        }
    /**
     * 来源
     */
    @Bindable var source:String?=null
        set(value) {

            field=value
            notifyPropertyChanged(BR.source)
        }
    @Ignore
    @Bindable var evaluations:List<Evaluation> = emptyList()
        set(value) {

            field=value
            notifyPropertyChanged(BR.evaluations)
        }

    /**
     * 1卒中2胸痛
     */
    @Bindable var symptom_Code:Int=2
        set(value) {

            field=value
            notifyPropertyChanged(BR.symptom_Code)
        }

    /**
     * 1卒中2胸痛
     */
    @Ignore
    @Bindable var attackClock:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.attackClock)
        }

    /**
     * 1卒中2胸痛
     */
    @Ignore
    @Bindable var attackProgress:Int=0
        set(value) {

            field=value
            notifyPropertyChanged(BR.attackProgress)
        }


    /**
     * 状态
     */
    @Bindable var statu:String?=null
        set(value) {

            field=value
            notifyPropertyChanged(BR.statu)
        }

    /**
     * 生命体征
     */
    @Transient
    @Ignore
    @Bindable var vitalSigns:VitalSign?=null
        set(value) {

            field=value
            notifyPropertyChanged(BR.vitalSigns)
        }

    override fun toString(): String {

        var s=""
        vitalSigns?.apply {
            s+=("生命体征："+this.toString())
        }

        if(evaluations.isNotEmpty()){
            s+=(if(s.isNotEmpty())"\n" else "")+evaluations?.map { it.name }?.joinToString()
        }
        return s
    }

}