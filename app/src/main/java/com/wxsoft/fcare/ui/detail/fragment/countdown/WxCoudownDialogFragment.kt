package com.wxsoft.fcare.ui.detail.fragment.countdown

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment


open class WxCoudownDialogFragment : AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return WxCoudownDialog(context)
    }
}