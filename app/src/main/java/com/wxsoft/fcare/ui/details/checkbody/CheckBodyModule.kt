package com.wxsoft.fcare.ui.details.checkbody

import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class CheckBodyModule {
    @Binds
    @IntoMap
    @ViewModelKey(CheckBodyViewModel::class)
    abstract fun bindCheckBodyViewModel(viewModel: CheckBodyViewModel): ViewModel
}