package com.wxsoft.fcare.ui.main.fragment.task.searchtask

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.main.fragment.task.TaskViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class SearchTaskModule {
    @Binds
    @IntoMap
    @ViewModelKey(TaskViewModel::class)
    abstract fun bindTaskViewModel(viewModel: TaskViewModel): ViewModel
}