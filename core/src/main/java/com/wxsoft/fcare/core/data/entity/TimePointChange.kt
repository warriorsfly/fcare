package com.wxsoft.fcare.core.data.entity

data class TimePointChange (
    val patientId:String,
    var time:String?,
    /**
     * 字段对应的实体表字段名,需要在时间点编辑接口中回传
     */
    val tableName:String,
    /**
     * 字段对应的实体表字段名,需要在时间点编辑接口中回传
     */
    val fieldName:String
)