package com.wxsoft.fcare.ui.details.complication

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ComplicationModule {
    @Binds
    @IntoMap
    @ViewModelKey(ComplicationViewModel::class)
    abstract fun bindComplicationViewModel(viewModel: ComplicationViewModel): ViewModel
}