package com.wxsoft.fcare.core.data.entity

open class Record<T>(val typeId:String="",
                     val typeName:String="",
                     val items:List<T> = emptyList(),
                     @Transient
                     open var tint:Int=0)