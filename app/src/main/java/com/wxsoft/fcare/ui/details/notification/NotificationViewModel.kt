package com.wxsoft.fcare.ui.details.notification

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel;
import com.wxsoft.fcare.ui.ICommonPresenter

class NotificationViewModel : ViewModel(),ICommonPresenter {
    override val title: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val clickableTitle: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val clickable: LiveData<Boolean>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun click() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    // TODO: Implement the ViewModel
}
