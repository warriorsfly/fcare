package com.wxsoft.fcare.core.data.entity

import com.google.gson.annotations.SerializedName

data class Pacs(val id:String="",
                var patientId:String="",
                var applyNo:String="",

                @SerializedName("apply_Time")
                var applyTime	:String?=null,
                @SerializedName("notice_Time")
                var noticeTime	:String?=null,
                @SerializedName("finish_Time")
                var finishTime	:String?=null,
                @SerializedName("doctor_Arrive_Time")
                var doctorArriveAt	:String?=null,
                @SerializedName("patient_Arrive_Time")
                var patientArriveAt	:String?=null,
                @SerializedName("start_Scan_Time")
                var startScanAt	:String?=null,
                @SerializedName("end_Scan_Time")
                var endScanAt	:String?=null,
                @SerializedName("report_Time")
                var reportAt	:String?=null,
                @SerializedName("check_Report")
                var checkedAt	:String?=null,
                @SerializedName("report_Result")
                var result	:String?=null,
                @SerializedName("pacS_Type")
                var type	:Int?=null,

                var createdDate	:String?=null,
                var modifiedDate	:String?=null,
                var createrId	:String?=null,
                var createrName	:String?=null,
                var modifierId	:String?=null,
                var modifierName	:String?=null
)