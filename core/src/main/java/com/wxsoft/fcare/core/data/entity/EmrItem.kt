package com.wxsoft.fcare.core.data.entity

import com.google.gson.annotations.SerializedName

/**
 * 病人emr
 */
data class EmrItem(val id:String) {

        @SerializedName("actionCode")var  code:String?=null
        var  name:String?=null
        @SerializedName("excutedTime")
        var completedAt:String?=null
                get() {
                        return field?.substring(11,16)
                }
        set(value) {field=value}

        @SerializedName("hasExcuted")var  done:Boolean=false
        @SerializedName("data")var  result:Any?=null

}