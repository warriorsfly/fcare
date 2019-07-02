package com.wxsoft.fcare.ui.details.strategy

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class FastModule {
    @Binds
    @IntoMap
    @ViewModelKey(FastViewModel::class)
    abstract fun bindfastViewModel(viewModel: FastViewModel): ViewModel
}