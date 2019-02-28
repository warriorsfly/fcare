package com.wxsoft.fcare.core.data.entity

data class TimeQuality (
    /**
     * 质控项名称
     */
    val title:String,
    /**
     * 质控项分值
     */
    val score:Int,
    /**
     * 是否超出标准
     */
    val overflow:Boolean=false)