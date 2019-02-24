package com.wxsoft.fcare.core.data.entity

data class NewTimeLine (
    @Transient
    val starter:Boolean=false,
    var excutedAt:String?=null,
    val eventName:String
)