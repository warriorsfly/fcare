package com.wxsoft.fcare.ui.details.operation

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
internal abstract class OperationModule {
    @Binds
    @IntoMap
    @ViewModelKey(OperationViewModel::class)
    abstract fun bindOperationViewModel(viewModel: OperationViewModel): ViewModel
}