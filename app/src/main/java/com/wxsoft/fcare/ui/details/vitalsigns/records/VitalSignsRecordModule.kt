package com.wxsoft.fcare.ui.details.vitalsigns.records

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class VitalSignsRecordModule {
    @Binds
    @IntoMap
    @ViewModelKey(VitalSignsRecordViewModel::class)
    abstract fun bindVitalSignsRecordViewModel(viewModel: VitalSignsRecordViewModel): ViewModel
}