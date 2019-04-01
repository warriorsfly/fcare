package com.wxsoft.fcare.core.data.entity

data class Complication (val id: String,
                        var patientId:String,
                        var complicationCode:String,
                         var complicationCode_Name:String,
                        var sceneType:String
)