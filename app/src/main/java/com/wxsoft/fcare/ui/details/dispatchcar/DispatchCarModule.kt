package com.wxsoft.fcare.ui.details.dispatchcar

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class DispatchCarModule {

    @Binds
    @IntoMap
    @ViewModelKey(DispatchCarViewModel::class)
    abstract fun bindDispatchCarViewModel(viewModel: DispatchCarViewModel): ViewModel
}