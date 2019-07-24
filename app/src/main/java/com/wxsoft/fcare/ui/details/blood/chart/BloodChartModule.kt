package com.wxsoft.fcare.ui.details.blood.chart

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.details.blood.BloodViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class BloodChartModule {
    @Binds
    @IntoMap
    @ViewModelKey(BloodChartViewModel::class)
    abstract fun bindBloodChartViewModel(viewModel: BloodChartViewModel): ViewModel
}