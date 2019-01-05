package com.wxsoft.fcare.ui.details.dispatchcar

import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsViewModel
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