package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.utils.DateTimeUtils
import java.io.Serializable

data class Patient(val id:String=""):BaseObservable(), Serializable {

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
            if (value){name="三无患者" }else {
                name=""
                idcard = ""
                age = 0
            }
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
    /*
     * 门诊号
     */
    @SerializedName("outpatient_Id")
    @get:Bindable
    var outpatientId:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.outpatientId)
        }
    /*
     * 住院号
     */
    @SerializedName("inpatient_Id")
    @get:Bindable
    var inpatientId:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.inpatientId)
        }

    @SerializedName("idcard")
    @get:Bindable
    var idcard:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.idcard)
            if(field.trim().length==18) {
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

    @SerializedName("arrived_Hospital_Time")
    @get:Bindable
    var arrivedHospitalTime: String?=null
        set(value) {
            field = value
            notifyPropertyChanged(BR.arrivedHospitalTime)
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


    @SerializedName("diagnosisCode2_Name")
    @get:Bindable
    var diagnosis2Name: String?=null
        set(value) {
            if(value == "代码不存在")return
            field = value
            notifyPropertyChanged(BR.diagnosis2Name)
        }


    @get:Bindable
    var memo: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.memo)
        }
//    @SerializedName("register_Id")
//    @get:Bindable
//    var registerId: String=""
//        set(value) {
//            field = value
//            notifyPropertyChanged(BR.registerId)
//        }

    @SerializedName("statu")
    @get:Bindable
    var status: Int=0
        set(value) {
            field = value
            notifyPropertyChanged(BR.status)
        }
    @get:Bindable
    var currentScene: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.currentScene)
        }
    @SerializedName("currentScene_Name")
    @get:Bindable
    var currentSceneName: String = ""
        set(value) {
            field = value
            timing=field<"223-4"
            notifyPropertyChanged(BR.currentSceneName)
        }

    @Transient
    @get:Bindable
    var timing: Boolean=false
        set(value) {
            field = value
            notifyPropertyChanged(BR.timing)
        }


    @get:Bindable
    @SerializedName("coming_Way_Code_Name")
    var comingWay_Name: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.comingWay_Name)
        }
    @get:Bindable
    var comingWay: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.comingWay)
        }
//    @get:Bindable
//    var createdBy: String=""
//        set(value) {
//            field = value
//            notifyPropertyChanged(BR.createdBy)
//        }

    @get:Bindable
    var createdDate: String?=null
        set(value) {
            field = value
            notifyPropertyChanged(BR.createdDate)
        }
    @get:Bindable
    var createrId: String?=null
        set(value) {
            field = value
            notifyPropertyChanged(BR.createrId)
        }

//    @Embedded
    @Transient
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

    @get:Bindable
    @SerializedName("strategyCode_Name")
    var strategyName: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.strategyName)
        }


    @get:Bindable
    var lsh: String=""
    // 卒中fast评分
    var stroke120Id: String=""

    @Bindable
    @Transient
    var checked:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.checked)
        }

}