package com.wxsoft.fcare.ui.selecter

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.discharge.DisChargeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class SelecterOfOneModule {
    @Binds
    @IntoMap
    @ViewModelKey(SelecterOfOneViewModel::class)
    abstract fun bindSelecterOfOneViewModel(viewModel: SelecterOfOneViewModel): ViewModel
}