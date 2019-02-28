package com.wxsoft.fcare.core.data.entity

/**
 * {@link WorkingActivity}
 */
data class WorkOperation(val id:String,
                         val name:String,
                         val actionCode:String,
                         val parameter:String,
                         val memo:String,
                         val isEnable:Boolean,
                         val hasExcuted:Boolean,
                         val excutedTime:String,
                         @Transient
                         var tint:Int=0,
                         @Transient
                         var ico:Int=0)