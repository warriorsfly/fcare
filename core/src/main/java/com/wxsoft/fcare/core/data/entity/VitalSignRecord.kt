package com.wxsoft.fcare.core.data.entity

data class VitalSignRecord (var typeId:String,
                            var typeName:String,
                            var items:List<VitalSign>
                            )