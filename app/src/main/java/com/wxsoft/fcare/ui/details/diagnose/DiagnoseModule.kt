package com.wxsoft.fcare.ui.details.diagnose


import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.details.diagnose.diagnosenew.DiagnoseNewViewModel
import com.wxsoft.fcare.ui.details.diagnose.diagnosenew.treatment.TreatmentOptionsActivity
import com.wxsoft.fcare.ui.details.diagnose.diagnosenew.treatment.TreatmentOptionsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class DiagnoseModule {
    @Binds
    @IntoMap
    @ViewModelKey(DiagnoseViewModel::class)
    abstract fun bindDiagnoseViewModel(viewModel: DiagnoseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DiagnoseNewViewModel::class)
    abstract fun bindDiagnoseNewViewModel(viewModel: DiagnoseNewViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TreatmentOptionsViewModel::class)
    abstract fun bindTreatmentOptionsViewModel(viewModel: TreatmentOptionsViewModel): ViewModel
}