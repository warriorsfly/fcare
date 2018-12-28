package com.wxsoft.fcare.core.data.entity

import com.google.gson.annotations.SerializedName

data class Task (
    val id:String,
    var taskDate: String,
    var taskPosition: String,
    var startAt: String,
    var arriveAt: String,
    var firstMet: String,
    var returnAt: String,
    var arriveHosAt: String,
    var carId: String,
    var canceled: String,
    var taskStaffs: Array<TaskStaff>,
    var patients: Array<Patient>,
    @SerializedName("isCanceled")var hasCanceled: Boolean,

    var createdBy: String,
    var createdDate: String,
    var modifiedBy: String,
    var modifiedDate: String
)