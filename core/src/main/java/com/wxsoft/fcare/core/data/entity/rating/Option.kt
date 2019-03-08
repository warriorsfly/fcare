package com.wxsoft.fcare.core.data.entity.rating

import com.google.gson.annotations.SerializedName

/**
 * 评分选项
 */
data class Option(val id:String,
                  val subjectId:String,
                  val name:String,
                  val score:Int,
                  @SerializedName("sortNum")
                  val index:Int,
                  @Transient
                  var checked:Boolean=false)