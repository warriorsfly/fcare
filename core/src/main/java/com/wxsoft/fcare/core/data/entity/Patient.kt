package com.wxsoft.fcare.core.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.utils.DateTimeUtils

@Entity(tableName = "patients")
data class Patient(@PrimaryKey val id:String=""):BaseObservable(){

    var taskId: String?=null

    @get:Bindable
    var name: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @SerializedName("three_Without")
    @get:Bindable
    var unKnown:Boolean=false
        set(value) {
            field = value
            notifyPropertyChanged(BR.unKnown)
            name="三无患者"
        }

    @SerializedName("hospital_Id")
    @get:Bindable
    var hospitalId:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.hospitalId)
        }

    @SerializedName("citizen_Card")
    @get:Bindable
    var citizen:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.citizen)
        }

    @SerializedName("wristband_Number")
    @get:Bindable
    var wristband:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.wristband)
        }

    @SerializedName("idcard")
    @get:Bindable
    var idcard:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.idcard)
            if(field.length==18) {
                age= DateTimeUtils.getAgeByCertId(field)
            }

        }

//    /**
//     * 01.卒中、02.胸痛
//     */
    @get:Bindable
    var firstMet:String?=null

        set(value) {
            field = value
            notifyPropertyChanged(BR.firstMet)
        }

    @get:Bindable
    var gender:Int=1
        set(value) {
            field = value
            notifyPropertyChanged(BR.gender)
        }

    fun setMale(male:Boolean){
        gender=if(male)1 else 2
    }

    fun getMale():Boolean{
       return gender==1
    }
    @SerializedName("age_Value")
    @get:Bindable
    var age:Int=0
        set(value) {
            field = value
            notifyPropertyChanged(BR.age)
        }

    @SerializedName("age_Unit")
    @get:Bindable
    var ageUnit:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ageUnit)
        }

    @SerializedName("contact_Phone")
    @get:Bindable
    var phone:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.phone)
        }

    @SerializedName("outpatient_Id")
    @get:Bindable
    var outId:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.outId)
        }

    @SerializedName("inpatient_Id")
    @get:Bindable
    var inId:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.inId)
        }

    @SerializedName("attack_Address")
    @get:Bindable
    var attackPosition:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.attackPosition)
        }

    @SerializedName("is_Null_Attack_Detail_Time")
    @get:Bindable
    var unKnowAttackingTime: Boolean=false
        set(value) {
            field = value
            notifyPropertyChanged(BR.unKnowAttackingTime)
        }

    @SerializedName("attack_Zone")
    @get:Bindable
    var attackZone: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.attackZone)
        }

    @SerializedName("attack_Time")
    @get:Bindable
    var attackTime: String?=null
        set(value) {
            field = value
            notifyPropertyChanged(BR.attackTime)
        }

    @SerializedName("is_Help")
    @get:Bindable
    var callForHelp: Boolean= false
        set(value) {
            field = value
            notifyPropertyChanged(BR.callForHelp)
        }

    @SerializedName("help_Date")
    @get:Bindable
    var helpDate: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.helpDate)
        }

    @SerializedName("help_Code")
    @get:Bindable
    var helpCode: Int=0
        set(value) {
            field = value
            notifyPropertyChanged(BR.helpCode)
        }

    @SerializedName("diagnosis_Code")
    @get:Bindable
    var diagnosisCode: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosisCode)
        }

    @SerializedName("diagnosis_Code_Name")
    @get:Bindable
    var diagnosisName: String=""
        set(value) {
            if(value == "代码不存在")return
            field = value
            notifyPropertyChanged(BR.diagnosisName)
        }


    @get:Bindable
    var memo: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.memo)
        }
    @SerializedName("register_Id")
    @get:Bindable
    var registerId: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.registerId)
        }

    @SerializedName("statu")
    @get:Bindable
    var status: Int=0
        set(value) {
            field = value
            notifyPropertyChanged(BR.status)
        }

    @get:Bindable
    var createdBy: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.createdBy)
        }

    @get:Bindable
    var createdDate: String?=null
        set(value) {
            field = value
            notifyPropertyChanged(BR.createdDate)
        }

//    @Embedded
    @Ignore
    var attachments:List<Attachment> = emptyList()

    @get:Bindable
    var modifiedBy: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedBy)
        }
    @get:Bindable
    var modifiedDate: String?=null
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }
}