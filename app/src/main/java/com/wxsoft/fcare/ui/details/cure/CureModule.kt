package com.wxsoft.fcare.ui.details.cure

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class CureModule {
    @Binds
    @IntoMap
    @ViewModelKey(CureViewModel::class)
    abstract fun bindCureViewModel(viewModel: CureViewModel): ViewModel

}