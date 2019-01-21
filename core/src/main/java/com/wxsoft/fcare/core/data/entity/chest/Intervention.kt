package com.wxsoft.fcare.core.data.entity.chest

import com.google.gson.annotations.SerializedName

data class Intervention(val id:String="",
                        var patientId:String="",
                        /**
                         * 决定 医生Id
                         */
                        @SerializedName("doctor_Id")
                        var doctorId	:String="",
                        /**
                         * 决定 医生Id
                         */
                        @SerializedName("doctor_Name")
                        var doctorName	:String="",
                        /**
                         * 决定 医生Id
                         */
                        var intervention_Person_Id	:String="",
                        var intervention_Person	:String="",
                        var decision_Operation_Time	:String="",
                        var informedConsentId	:String="",
                        /**
                         * 启动 导管室
                         */
                        @SerializedName("start_Conduit_Time")
                        var startConduitTime	:String="",
                        var start_Agree_Time	:String="",
                        var sign_Agree_Time	:String="",
                        @SerializedName("activate_Conduit_Time")
                        var activateConduitTime	:String="",
                        @SerializedName("arrive_Conduit_Time")
                        var arriveConduitTime	:String="",

                        /**
                         * 造影
                         */
                        @SerializedName("start_Radiography_Time")
                        var startRadiographyTime	:String="",
                        @SerializedName("end_Radiography_Time")
                        var endRadiographyTime	:String="",

                        var again_Sign_Agree_Time	:String="",
                        /**
                         * 球囊扩张
                         */

                        @SerializedName("balloon_Expansion_Time")
                        var balloonExpansionTime	:String="",
                        @SerializedName("start_Operation_Time")
                        var startOperationTime	:String="",
                        @SerializedName("end_Operation_Time")
                        var endOperationTime	:String="",
                        /**
                         * 穿刺
                         */
                        @SerializedName("start_Puncture_Time")
                        var startPunctureTime	:String="",
                        @SerializedName("successPunctureTime")
                        var successPunctureTime	:String="",
                        @SerializedName("leave_Conduit_Time")
                        var leave	:String="",
                        /**
                         * 大门到球囊扩张
                         */
                        var d2B	:String="",
                        var createdDate	:String=""
                        )