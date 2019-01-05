package com.wxsoft.fcare.core.data.entity

import com.google.gson.annotations.SerializedName

/**
 * 病人emr
 */
data class EmrItem(val id:String) {

        @SerializedName("actionCode")var  code:String?=null
        var  name:String?=null
        @SerializedName("")var  complatedAt:String?=null
        @SerializedName("hasExcuted")var  done:Boolean=false
        @SerializedName("data")var  result:Any?=null

}