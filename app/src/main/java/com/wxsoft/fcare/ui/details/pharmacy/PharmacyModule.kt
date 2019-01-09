package com.wxsoft.fcare.ui.details.pharmacy

import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
internal abstract class PharmacyModule {
    @Binds
    @IntoMap
    @ViewModelKey(PharmacyViewModel::class)
    abstract fun bindPharmacyViewModel(viewModel: PharmacyViewModel): ViewModel
}