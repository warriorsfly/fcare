package com.wxsoft.fcare.ui.details.blood.pressure

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class BloodPressureModule {
    @Binds
    @IntoMap
    @ViewModelKey(BloodPressureViewModel::class)
    abstract fun bindSelectDrugsViewModel(viewModel: BloodPressureViewModel): ViewModel
}