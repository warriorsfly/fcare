package com.wxsoft.fcare.core.data.entity

data class Hospital(val id:String,
                    var name:String,
                    var address:String,
                    /**
                     *救治能力，能力分级
                     */
                    var treatment_Ability:String,
                    var tel_Cz:String,
                    var tel_Xt:String,
                    var grade:String,
                    var memo:String)