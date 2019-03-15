package com.wxsoft.fcare.core.data.entity

data class Record<T>(var typeId:String,
                     var typeName:String,
                     var items:List<T>,
                     @Transient
                     var tint:Int=0)