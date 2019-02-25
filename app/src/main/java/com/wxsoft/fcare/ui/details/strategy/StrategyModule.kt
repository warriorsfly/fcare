package com.wxsoft.fcare.ui.details.strategy

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class StrategyModule {
    @Binds
    @IntoMap
    @ViewModelKey(StrategyViewModel::class)
    abstract fun bindStrategyViewModel(viewModel: StrategyViewModel): ViewModel
}