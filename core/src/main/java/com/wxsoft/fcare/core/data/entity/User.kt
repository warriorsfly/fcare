package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class User(
    var id:String,
    var userName: String="",
    var password: String="",
    var trueName: String ="",
    var tel: String ="",
    var email: String ="",
    var weiXin: String="",
    var deptId: String ="",
    var deptName: String ="",
    var postId: String ="",
    var isActive: Boolean=true,
    var postName: String ="",
    var hospitalId: String ="",
    var hospitalName: String ="",
    var memo: String ="",
    var jgAccount: String ="",
    var userRoles: Array<UserRole> = emptyArray(),
    var createdDate: String ="",
    var modifiedDate: String ="",
    var createrId: String ="",
    var createrName: String ="",
    var modifierId: String ="",
    var modifierName: String =""): BaseObservable() {

    @Transient
    @get:Bindable
    var status: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.status)
        }

    @Bindable
    @Transient
    var checked:Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.checked)
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (userName != other.userName) return false
        if (password != other.password) return false
        if (trueName != other.trueName) return false
        if (tel != other.tel) return false
        if (email != other.email) return false
        if (weiXin != other.weiXin) return false
        if (deptId != other.deptId) return false
        if (deptName != other.deptName) return false
        if (postId != other.postId) return false
        if (isActive != other.isActive) return false
        if (postName != other.postName) return false
        if (hospitalId != other.hospitalId) return false
        if (hospitalName != other.hospitalName) return false
        if (memo != other.memo) return false
        if (jgAccount != other.jgAccount) return false
        if (!userRoles.contentEquals(other.userRoles)) return false
        if (createdDate != other.createdDate) return false
        if (modifiedDate != other.modifiedDate) return false
        if (createrId != other.createrId) return false
        if (createrName != other.createrName) return false
        if (modifierId != other.modifierId) return false
        if (modifierName != other.modifierName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userName.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + trueName.hashCode()
        result = 31 * result + tel.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + weiXin.hashCode()
        result = 31 * result + deptId.hashCode()
        result = 31 * result + deptName.hashCode()
        result = 31 * result + postId.hashCode()
        result = 31 * result + isActive.hashCode()
        result = 31 * result + postName.hashCode()
        result = 31 * result + hospitalId.hashCode()
        result = 31 * result + hospitalName.hashCode()
        result = 31 * result + memo.hashCode()
        result = 31 * result + jgAccount.hashCode()
        result = 31 * result + userRoles.contentHashCode()
        result = 31 * result + createdDate.hashCode()
        result = 31 * result + modifiedDate.hashCode()
        result = 31 * result + createrId.hashCode()
        result = 31 * result + createrName.hashCode()
        result = 31 * result + modifierId.hashCode()
        result = 31 * result + modifierName.hashCode()
        return result
    }
}