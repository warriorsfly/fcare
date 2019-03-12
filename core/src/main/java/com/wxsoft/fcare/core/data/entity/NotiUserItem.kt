package com.wxsoft.fcare.core.data.entity

data class NotiUserItem (
    var id:String,
    var name:String,
    var memo:String,
    var hospitalId:String,
    var diagnosisCode:String,
    var users:List<User>
)