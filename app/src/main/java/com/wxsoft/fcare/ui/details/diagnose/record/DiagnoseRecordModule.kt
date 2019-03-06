package com.wxsoft.fcare.ui.details.diagnose.record

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class DiagnoseRecordModule {
    @Binds
    @IntoMap
    @ViewModelKey(DiagnoseRecordViewModel::class)
    abstract fun bindDiagnoseRecordViewModel(viewModel: DiagnoseRecordViewModel): ViewModel
}