package com.wxsoft.fcare.core.data.entity.rating

data class Grade(
    val id:String,
    val ratingId:String,
    val scoreStart:Int,
    val scoreEnd:Int,
    val gradeDesc:String,
    val foreColor:String
)