package com.wxsoft.fcare.core.data.entity.chest

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

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

                        var decision_Operation_Time	:String?=null,
                        var informedConsentId	:String="",
                        /**
                         * 启动 导管室
                         */
                        @SerializedName("start_Conduit_Time")
                        var startConduitTime	:String?=null,
                        var start_Agree_Time	:String?=null,
                        var sign_Agree_Time	:String?=null,
                        @SerializedName("activate_Conduit_Time")
                        var activateConduitTime	:String?=null,
                        @SerializedName("arrive_Conduit_Time")
                        var arriveConduitTime	:String?=null,

                        /**
                         * 造影
                         */
                        @SerializedName("start_Radiography_Time")
                        var startRadiographyTime	:String?=null,
                        @SerializedName("end_Radiography_Time")
                        var endRadiographyTime	:String?=null,

                        var again_Sign_Agree_Time	:String?=null,
                        /**
                         * 球囊扩张
                         */

                        @SerializedName("balloon_Expansion_Time")
                        var balloonExpansionTime	:String?=null,
                        @SerializedName("start_Operation_Time")
                        var startOperationTime	:String?=null,
                        @SerializedName("end_Operation_Time")
                        var endOperationTime	:String?=null,
                        /**
                         * 穿刺
                         */
                        @SerializedName("start_Puncture_Time")
                        var startPunctureTime	:String?=null,
                        @SerializedName("successPunctureTime")
                        var successPunctureTime	:String?=null,
                        @SerializedName("leave_Conduit_Time")
                        var leave	:String?=null,
                        /**
                         * 大门到球囊扩张
                         */
                        var d2B	:String?=null,
                        var createdDate	:String?=null
                        ):BaseObservable(){
    @get:Bindable
    @SerializedName("intervention_Person_Id")
    var interventionMateIds	:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.interventionMateIds)
        }
    @get:Bindable
    @SerializedName("intervention_Person")
    var interventionMates	:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.interventionMates)
        }

}