package com.wxsoft.fcare.core.data.entity

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.google.gson.annotations.SerializedName

/**
 * 数据字典
 */
data class BaseData (
    val id:String,
    var name: ObservableField<String>,
    @SerializedName("sortNum")var sort: ObservableInt,
    @SerializedName("isEnable")var enabled: ObservableBoolean,
    @SerializedName("parentId")var parent: ObservableField<String>,
    @SerializedName("groupCode")var group: ObservableField<String>
)