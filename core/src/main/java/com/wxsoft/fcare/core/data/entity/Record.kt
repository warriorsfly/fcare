package com.wxsoft.fcare.core.data.entity

open class Record<T>(open val typeId:String="",
                     open val typeName:String="",
                     open val items:List<T> = emptyList(),
                     @Transient
                     open var tint:Int=0)