package com.wxsoft.fcare.core.data.entity.rating

/**
 * 场景评分信息
 */
data class ScencelyRatingResult(val typeId:String,
                                val typeName:String,
                                val items:List<RatingResult>)