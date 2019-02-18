package com.wxsoft.fcare.ui.details.informedconsent

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.details.informedconsent.addinformed.AddInformedConsentViewModel
import com.wxsoft.fcare.ui.details.informedconsent.informeddetails.InformedConsentDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class InformedConsentModule {

    @Binds
    @IntoMap
    @ViewModelKey(InformedConsentViewModel::class)
    abstract fun bindInformedConsentViewModel(viewModel: InformedConsentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddInformedConsentViewModel::class)
    abstract fun bindAddInformedConsentViewModel(viewModel: AddInformedConsentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InformedConsentDetailsViewModel::class)
    abstract fun bindInformedConsentDetailsViewModel(viewModel: InformedConsentDetailsViewModel): ViewModel

}