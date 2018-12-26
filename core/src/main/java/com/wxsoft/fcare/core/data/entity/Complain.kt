package com.wxsoft.fcare.core.data.entity

import java.sql.Date

/**
 * 主诉
 */
data class Complain(
    val id:String,
    var patientId :String,
    var symptomId :String,
    var createdBy :String,
    var createdDate :Date,
    var modifiedBy :String,
    var modifiedDate : Date
)