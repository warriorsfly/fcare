package com.wxsoft.fcare.data.entity

data class Account(var id:String) {

    var userName: String = ""
    var password: String = ""
    var trueName: String = ""
    var tel: String = ""
    var email: String = ""
    var weiXin: String = ""
    var deptId: String = ""
    var deptName: String = ""
    var postId: String = ""
    var isActive: Boolean=false
    var postName: String = ""
    var hospitalId: String = ""
    var hospitalName: String = ""
    var memo: String = ""
    var jgAccount: String = ""
    var createdDate: String = ""
    var modifiedDate: String = ""
    var createrId: String = ""
    var createrName: String = ""
    var modifierId: String = ""
    var modifierName: String = ""
}