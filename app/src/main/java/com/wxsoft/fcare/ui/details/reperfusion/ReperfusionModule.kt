package com.wxsoft.fcare.ui.details.reperfusion

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ReperfusionModule {

    @Binds
    @IntoMap
    @ViewModelKey(ReperfusionViewModel::class)
    abstract fun bindReperfusionViewModel(viewModel: ReperfusionViewModel): ViewModel

}