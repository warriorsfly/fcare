package com.wxsoft.fcare.ui.calling

import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class CallingModule {

    @Binds
    @IntoMap
    @ViewModelKey(CallingViewModel::class)
    abstract fun bindCallingViewModel(viewModel: CallingViewModel): ViewModel

}