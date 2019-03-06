package com.wxsoft.fcare.core.data.entity

class DiagnoseRecord (var typeId:String,
                      var typeName:String,
                      var items:List<Diagnosis>,
                      @Transient
                      var tint:Int=0
)