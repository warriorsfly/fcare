package com.wxsoft.fcare.core.data.entity

data class VitalSignRecord (val id:String,
                            var typeId:String,
                            var typeName:String,
                            var items:List<VitalSign>
                            )