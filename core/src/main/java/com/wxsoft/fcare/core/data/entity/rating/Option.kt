package com.wxsoft.fcare.core.data.entity.rating

import com.google.gson.annotations.SerializedName

/**
 * 评分选项
 */
data class Option(val id:String,
                  val subjectId:String,
                  val name:String,
                  val score:Float,
                  @SerializedName("sortNum")val index:Int)