package com.wxsoft.fcare.ui.details.medicalhistory

import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class MedicalHistoryModule {
    @Binds
    @IntoMap
    @ViewModelKey(MedicalHistoryViewModel::class)
    abstract fun bindMedicalHistoryViewModel(viewModel: MedicalHistoryViewModel): ViewModel
}