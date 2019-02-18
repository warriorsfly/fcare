package com.wxsoft.fcare.ui

import androidx.lifecycle.LiveData

interface ICommonPresenter{
    var title:String
    val clickableTitle:String
    val clickable:LiveData<Boolean>
    fun click()
}