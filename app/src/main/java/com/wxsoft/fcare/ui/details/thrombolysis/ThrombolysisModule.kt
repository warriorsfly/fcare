package com.wxsoft.fcare.ui.details.thrombolysis

import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ThrombolysisModule {
    @Binds
    @IntoMap
    @ViewModelKey(ThrombolysisViewModel::class)
    abstract fun bindThrombolysisViewModel(viewModel: ThrombolysisViewModel): ViewModel

}