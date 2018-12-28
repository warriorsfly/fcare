package com.wxsoft.fcare.core.data.entity

data class User(
    var id:String,
    var userName: String ,
    var password: String ,
    var trueName: String ,
    var tel: String ,
    var email: String ,
    var weiXin: String ,
    var deptId: String ,
    var deptName: String ,
    var postId: String ,
    var isActive: Boolean,
    var postName: String ,
    var hospitalId: String ,
    var hospitalName: String ,
    var memo: String ,
    var jgAccount: String ,
    var userRoles: Array<UserRole> ,
    var createdDate: String ,
    var modifiedDate: String ,
    var createrId: String ,
    var createrName: String ,
    var modifierId: String ,
    var modifierName: String
)