package com.wxsoft.fcare.core.data.entity.rating

/**
 * 评分
 */
data class RatingResult(val id:String,
                        val patientId:String,
                        val ratingId:String,

                        val ratingName:String,
                        val score:Int,
                        val resultLevel:Int,
                        val sceneType:String,
                        val rating:String,
                        val createdDate:String?,
                        val createrName:String?,
                        val modifiedDate:String?,
                        val modifierName:String?,
                        val resultGrade:Grade?)