package com.wxsoft.fcare.core.data.entity

import com.google.gson.annotations.SerializedName

data class Task (
    val id:String,
    var taskDate: String,
    var name: String,
    @SerializedName("Three_Without")var unKnow: Boolean,
    @SerializedName("Hospital_Id")var hospitalId: String,
    @SerializedName("citizen_Card")var citizen: String,
    @SerializedName("wristband_Number")var wristband: String,

    @SerializedName("idcard")var idcard: String,
    var gender: String,

    @SerializedName("age_Value")var age: Int,
    @SerializedName("age_Unit")var ageUnit: String,
    @SerializedName("contact_Phone")var phone: String,
    @SerializedName("outpatient_Id")var outId: String,
    @SerializedName("inpatient_Id")var inId: String,
    @SerializedName("attack_Address")var attackPosition: String,
    @SerializedName("is_Null_Attack_Detail_Time")var unKnowAttackingTime: Boolean,
    @SerializedName("attack_Zone")var attackZone: String,
    @SerializedName("is_Help")var callForHelp: Boolean,
    @SerializedName("help_Date")var helpDate: String,
    @SerializedName("help_Code")var helpCode: Int,
    @SerializedName("is_Source")var source: Boolean,
    var memo: String,
    @SerializedName("register_Id")var registerId: String,
    @SerializedName("data_Statu")var status: Int,
    var createdBy: String,
    var createdDate: String,
    var modifiedBy: String,
    var modifiedDate: String
)