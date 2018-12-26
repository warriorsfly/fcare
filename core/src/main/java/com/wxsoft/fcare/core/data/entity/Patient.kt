package com.wxsoft.fcare.core.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.databinding.*
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

@Entity(tableName = "patients")
data class Patient(@PrimaryKey val id:String):BaseObservable(){

    var taskId: String=""

    @get:Bindable
    var name: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("Three_Without")
    @get:Bindable
    var unKnow:Boolean=false
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("Hospital_Id")
    @get:Bindable
    var hospitalId:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("citizen_Card")
    @get:Bindable
    var citizen:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("wristband_Number")
    @get:Bindable
    var wristband:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("idcard")
    @get:Bindable
    var idcard:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @get:Bindable
    var gender:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("age_Value")
    @get:Bindable
    var age:Int=0
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("age_Unit")
    @get:Bindable
    var ageUnit:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("contact_Phone")
    @get:Bindable
    var phone:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("outpatient_Id")
    @get:Bindable
    var outId:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("inpatient_Id")
    @get:Bindable
    var inId:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("attack_Address")
    @get:Bindable
    var attackPosition:String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("is_Null_Attack_Detail_Time")
    @get:Bindable
    var unKnowAttackingTime: Boolean=false
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("attack_Zone")
    @get:Bindable
    var attackZone: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("is_Help")
    @get:Bindable
    var callForHelp: Boolean= false
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("help_Date")
    @get:Bindable
    var helpDate: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("help_Code")
    @get:Bindable
    var helpCode: Int=0
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("is_Source")
    @get:Bindable
    var source: Boolean=false
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }


    @get:Bindable
    var memo: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }
    @SerializedName("register_Id")
    @get:Bindable
    var registerId: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @SerializedName("data_Statu")
    @get:Bindable
    var status: Int=0
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @get:Bindable
    var createdBy: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @get:Bindable
    var createdDate: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }

    @get:Bindable
    var modifiedBy: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }
    @get:Bindable
    var modifiedDate: String=""
        set(value) {
            field = value
            notifyPropertyChanged(BR.modifiedDate)
        }
}