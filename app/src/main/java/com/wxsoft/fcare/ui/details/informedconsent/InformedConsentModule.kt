package com.wxsoft.fcare.ui.details.informedconsent

import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class InformedConsentModule {

    @Binds
    @IntoMap
    @ViewModelKey(InformedConsentViewModel::class)
    abstract fun bindInformedConsentViewModel(viewModel: InformedConsentViewModel): ViewModel
}