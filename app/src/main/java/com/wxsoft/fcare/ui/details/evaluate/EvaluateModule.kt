package com.wxsoft.fcare.ui.details.evaluate

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class EvaluateModule {
    @Binds
    @IntoMap
    @ViewModelKey(EvaluateViewModel::class)
    abstract fun bindSelectDrugsViewModel(viewModel: EvaluateViewModel): ViewModel
}