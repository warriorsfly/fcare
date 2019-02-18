package com.wxsoft.fcare.ui.details.vitalsigns

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class VitalSignsModule {
    @Binds
    @IntoMap
    @ViewModelKey(VitalSignsViewModel::class)
    abstract fun bindVitalSignsViewModel(viewModel: VitalSignsViewModel): ViewModel
}