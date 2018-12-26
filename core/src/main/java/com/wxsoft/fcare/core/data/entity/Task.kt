package com.wxsoft.fcare.core.data.entity

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.google.gson.annotations.SerializedName

data class Task (
    val id:String,
    var taskDate: ObservableField<String>,
    var name: ObservableField<String>,
    @SerializedName("Three_Without")var unKnow: ObservableBoolean,
    @SerializedName("Hospital_Id")var hospitalId: ObservableField<String>,
    @SerializedName("citizen_Card")var citizen: ObservableField<String>,
    @SerializedName("wristband_Number")var wristband: ObservableField<String>,

    @SerializedName("idcard")var idcard: ObservableField<String>,
    var gender: ObservableField<String>,

    @SerializedName("age_Value")var age: ObservableInt,
    @SerializedName("age_Unit")var ageUnit: ObservableField<String>,
    @SerializedName("contact_Phone")var phone: ObservableField<String>,
    @SerializedName("outpatient_Id")var outId: ObservableField<String>,
    @SerializedName("inpatient_Id")var inId: ObservableField<String>,
    @SerializedName("attack_Address")var attackPosition: ObservableField<String>,
    @SerializedName("is_Null_Attack_Detail_Time")var unKnowAttackingTime: ObservableBoolean,
    @SerializedName("attack_Zone")var attackZone: ObservableField<String>,
    @SerializedName("is_Help")var callForHelp: ObservableBoolean,
    @SerializedName("help_Date")var helpDate: ObservableField<String>,
    @SerializedName("help_Code")var helpCode: ObservableInt,
    @SerializedName("is_Source")var source: ObservableBoolean,
    var memo: ObservableField<String>,
    @SerializedName("register_Id")var registerId: ObservableField<String>,
    @SerializedName("data_Statu")var status: ObservableInt,
    var createdBy: ObservableField<String>,
    var createdDate: ObservableField<String>,
    var modifiedBy: ObservableField<String>,
    var modifiedDate: ObservableField<String>
)