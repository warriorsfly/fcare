package com.wxsoft.fcare.data.entity

import java.sql.Array

class Doctor {

    var userName: String = ""
    var password: String = ""
    var trueName: String = ""
    var tel: String = ""
    var email: String = ""
    var weiXin: String = ""
    var deptId: String = ""
    var deptName: String = ""
    var postId: String = ""
    var isActive: Boolean = true
    var postName: String = ""
    var hospitalId: String = ""
    var hospitalName: String = ""
    var memo: String = ""
    var jgAccount: String = ""
    var userRoles:List<String> = emptyList()
    var page:Int = 1
    var limit:Int = 10
    var createdDate:String = ""
    var modifiedDate:String = ""
    var createrId:String = ""
    var createrName:String = ""
    var modifierId:String = ""
    var modifierName:String = ""
    var id:String = ""


}