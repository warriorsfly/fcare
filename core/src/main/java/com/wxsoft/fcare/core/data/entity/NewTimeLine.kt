package com.wxsoft.fcare.core.data.entity

data class NewTimeLine (
    @Transient
    val starter:Boolean=false,
    var excutedAt:String?=null,
    val eventName:String
)

data class TimeLineItem (
    val id:String,
    /**
     * operation event name
     * 节点名称，如NIHSS评分，溶栓后NIHSS评分或介入后NIHSS评分
     */
    val operation:String,
    /**
     * operation excuting time
     * 操作时间
     */
    var doneAt:String?=null,
    /**
     * if in quality control,show the quality control passed or unpassed memo
     * 备注，通常在质控操作未通过时候显示
     */
    val memo:String,
    /**
     * in quality control or not
     * 是否质控项
     */
    val inQc:Boolean,
    /**
     * pass quality control or not
     * 质控项是否通过
     */
    val qcPassed:Boolean?,
    /**
     * operation excuting at which case,
     * like [on 120,before diagnosis,before treatment and so on],
     * use for grouping
     * 场景，院前120，初步诊断，治疗前检查==，用来分组
     */
    val scene:String,
    /**
     * operation type,unique with operation name,stand for one end point of time line
     * 和操作名称一一对应，用来区分例如NIHSS评分，溶栓后NIHSS评分或介入后NIHSS评分
     */
    val type:String
)