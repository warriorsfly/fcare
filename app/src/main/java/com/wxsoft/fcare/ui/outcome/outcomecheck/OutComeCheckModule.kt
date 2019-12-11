package com.wxsoft.fcare.ui.outcome.outcomecheck

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.outcome.OutComeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class OutComeCheckModule {

    @Binds
    @IntoMap
    @ViewModelKey(OutComeCheckViewModel::class)
    abstract fun bindOutComeCheckViewModel(viewModel: OutComeCheckViewModel): ViewModel

}