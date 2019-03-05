package com.wxsoft.fcare.core.data.entity.rating

import com.google.gson.annotations.SerializedName

/**
 * 评分结果（一题一个记录）
 */
data class SubjectRecord(val id:String="",
                         val recordId:String,
                         val subjectId:String,
                         @SerializedName("myOptions")val selection:String,
                         val score:Int)