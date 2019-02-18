package com.wxsoft.fcare.ui.details.measures

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class MeasuresModule {

    @Binds
    @IntoMap
    @ViewModelKey(MeasuresViewModel::class)
    abstract fun bindMeasuresViewModel(viewModel: MeasuresViewModel): ViewModel
}