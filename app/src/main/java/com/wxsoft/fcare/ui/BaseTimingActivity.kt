package com.wxsoft.fcare.ui

import androidx.fragment.app.DialogFragment
import com.wxsoft.fcare.R
import com.wxsoft.fcare.ui.common.DingLikeTimePicker
import com.wxsoft.fcare.ui.common.ITimeSelected


abstract class BaseTimingActivity : BaseActivity(), ITimeSelected {

    protected var dialog: DingLikeTimePicker?=null

    protected fun createDialog(time:Long): DingLikeTimePicker {

        return DingLikeTimePicker(time,::selectTime)
    }
}