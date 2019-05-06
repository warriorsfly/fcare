package com.wxsoft.fcare.ui.details.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.NotiUserItem
import com.wxsoft.fcare.core.data.entity.Notify
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.User
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.NotificationApi
import com.wxsoft.fcare.core.data.remote.VitalSignApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class NotificationViewModel @Inject constructor(private val notificationApi: NotificationApi,
                                                override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {


    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            getUserItems()
        }
    var selectedUsers = ArrayList<User>()

    val userItems:LiveData<List<NotiUserItem>>
    private val loadUserItemsResult=MediatorLiveData<Response<List<NotiUserItem>>>()

    val checkedUsers:LiveData<List<User>>
    private val loadCheckedUsersResult=MediatorLiveData<List<User>>()

    val notify:LiveData<Notify>
    private val loadNotifyResult=MediatorLiveData<Notify>()

    val backToLast:LiveData<Boolean>
    private val initbackToLast = MediatorLiveData<Boolean>()

    init {
        backToLast = initbackToLast.map { it }
        userItems = loadUserItemsResult.map { it.result?: emptyList() }
        checkedUsers = loadCheckedUsersResult.map { it?: emptyList() }
        notify = loadNotifyResult.map { it ?: Notify(account.id,account.trueName,"","", emptyList()) }
        loadNotifyResult.value = null
    }


    fun getUserItems(){
        disposable.add(notificationApi.getNotifyUsers(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::loadUserItems,::error))
    }

    private fun loadUserItems(response: Response<List<NotiUserItem>>){
        loadUserItemsResult.value = response
    }

    fun selectedUser(user:User){
        user.checked = !user.checked
        if (user.checked){
            if (!selectedUsers.contains(user))selectedUsers.add(user)
            loadCheckedUsersResult.value = selectedUsers.toList()
        }else{
            if (selectedUsers.contains(user))selectedUsers.remove(user)
            loadCheckedUsersResult.value = selectedUsers.toList()
        }

    }


    fun deleteItem(user:User){
        user.checked = false
        selectedUsers.remove(user)
        loadCheckedUsersResult.value = selectedUsers.toList()
    }

    fun selectedGroup(item:NotiUserItem){
        item.checked = !item.checked
        item.users.map {
            it.checked = !item.checked
            selectedUser(it)
        }
    }



    fun toSubmit(){
        val arr = ArrayList<String>()
        checkedUsers.value?.map {
            arr.add(it.id)
        }
        notify.value?.receiverUserIds = arr.toList()
        notify.value?.patientId = patientId

        if (notify.value?.messageTemplateName.isNullOrEmpty()) {
            messageAction.value = Event("请选择通知类型")
            return
        }

        disposable.add(notificationApi.submitNotify(notify.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::saveResult,::error))
    }

    private fun saveResult(response: Response<String>){
        messageAction.value = Event(response.msg)
        if (response.success) initbackToLast.value = true
    }

}
