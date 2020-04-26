package com.wxsoft.fcare.core.data.entity

data class Message (
    val id:String,
    val subscribeId:String,
    val messageId:String,
    val messageSubject:String,
    val messageContent:String,
    val patientId:String,
    val patientName:String?="三无患者",
    val publishedTime:String?,
    val messageTag:String,
    /**
     * 0警告，警告类的消息
     * 1提醒，一般是一些定期事件到期或者逾期的提醒
     * 工作流事件，系统内用户通讯通知的事件
     */
    val messageType:Int,
    val optionalData:String,
    val userId:String)