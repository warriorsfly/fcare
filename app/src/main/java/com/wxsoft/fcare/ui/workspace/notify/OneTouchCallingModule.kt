package com.wxsoft.fcare.ui.workspace.notify

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class OneTouchCallingModule {
    @Binds
    @IntoMap
    @ViewModelKey(OneTouchCallingViewModel::class)
    abstract fun bindOneTouchCallingViewModel(viewModel: OneTouchCallingViewModel): ViewModel
}