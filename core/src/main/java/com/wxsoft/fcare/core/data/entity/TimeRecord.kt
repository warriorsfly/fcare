package com.wxsoft.fcare.core.data.entity

data class TimeRecord (
    val id:String,
    val excutedAt:String,
    val eventName:String,
    /**
     * 字段对应的实体表字段名,需要在时间点编辑接口中回传
     */
    val tableName:String,
    /**
     * 字段对应的实体表字段名,需要在时间点编辑接口中回传
     */
    val fieldName:String,
    val sort:Int,
    /**
     * 是否允许编辑
     */
    val editable:Boolean,
    /**
     *  fail:未达到质控规则或者有异常,ok:达成质控规则,null:普通状态
     */
    val eventStatus:String?,
    /**
     * 描述
     */
    val eventContent:String
)