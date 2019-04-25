package com.wxsoft.fcare.ui.hardwaredata

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class HardwareDataModule {
    @Binds
    @IntoMap
    @ViewModelKey(HardwareDataViewModel::class)
    abstract fun bindHardwareDataViewModel(viewModel: HardwareDataViewModel): ViewModel
}