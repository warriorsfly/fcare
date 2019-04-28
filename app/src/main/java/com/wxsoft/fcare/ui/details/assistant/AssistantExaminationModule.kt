package com.wxsoft.fcare.ui.details.assistant

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ChildFragmentScoped
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.details.assistant.troponin.TroponinViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class AssistantExaminationModule {
    @Binds
    @IntoMap
    @ViewModelKey(AssistantExaminationViewModel::class)
    abstract fun bindAssistantExaminationViewModel(viewModel: AssistantExaminationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TroponinViewModel::class)
    abstract fun bindTroponinViewModel(viewModel: TroponinViewModel): ViewModel

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeLisFragment(): LisFragment

}