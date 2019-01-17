package com.wxsoft.fcare.core.data.entity

data class Page<T>(val pageIndex:Int,
                   val pageSize:Int,
                   val totalCount:Int,
                   val totalPages:Int,
                   val indexFrom:Int,
                   val hasPreviousPage:Boolean,
                   val hasNextPage:Boolean,
                   val items:List<T>)