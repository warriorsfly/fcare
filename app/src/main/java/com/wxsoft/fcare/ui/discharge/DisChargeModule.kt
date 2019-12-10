package com.wxsoft.fcare.ui.discharge
import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class DisChargeModule {
    @Binds
    @IntoMap
    @ViewModelKey(DisChargeViewModel::class)
    abstract fun bindDisChargeViewModel(viewModel: DisChargeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DisChargeSonViewModel::class)
    abstract fun bindDisChargeSonViewModel(viewModel: DisChargeSonViewModel): ViewModel

}