package com.wxsoft.fcare.core.data.entity

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
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