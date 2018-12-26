package com.wxsoft.fcare.core.data.entity

data class Response<T>(val success: Boolean) {

    val msg:String=""
    var result:T?=null
}