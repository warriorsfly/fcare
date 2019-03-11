package com.wxsoft.fcare.ui.details.notification

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class NotificationModule {
    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel::class)
    abstract fun bindNotificationViewModel(viewModel: NotificationViewModel): ViewModel
}