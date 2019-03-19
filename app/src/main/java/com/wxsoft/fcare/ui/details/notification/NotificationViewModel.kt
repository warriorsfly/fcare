package com.wxsoft.fcare.ui.details.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.NotiUserItem
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.User
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.NotificationApi
import com.wxsoft.fcare.core.data.remote.VitalSignApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
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

    init {
        userItems = loadUserItemsResult.map { it.result?: emptyList() }
        checkedUsers = loadCheckedUsersResult.map { it?: emptyList() }
    }


    fun getUserItems(){
        disposable.add(notificationApi.getNotifyUsers(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::loadUserItems,::error))
    }

    private fun loadUserItems(response: Response<List<NotiUserItem>>){
        loadUserItemsResult.value = response
//        userItems.value?.map {
//            item->
//            item.users.map {
//                selectedUsers.add(it)
//            }
//        }
//        loadCheckedUsersResult.value = selectedUsers
    }

    fun selectedUser(user:User){
        user.checked = !user.checked
        if (user.checked){
            selectedUsers.add(user)
            loadCheckedUsersResult.value = selectedUsers.toList()
        }else{
            selectedUsers.remove(user)
            loadCheckedUsersResult.value = selectedUsers.toList()
        }

    }


    fun deleteItem(user:User){
        user.checked = false
        selectedUsers.remove(user)
        loadCheckedUsersResult.value = selectedUsers.toList()
    }
}
