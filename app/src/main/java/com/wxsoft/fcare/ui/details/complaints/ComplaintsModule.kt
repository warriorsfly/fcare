package com.wxsoft.fcare.ui.details.complaints

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ComplaintsModule {
    @Binds
    @IntoMap
    @ViewModelKey(ComplaintsViewModel::class)
    abstract fun bindComplaintsViewModel(viewModel: ComplaintsViewModel): ViewModel
}