package com.wxsoft.fcare.ui.details.comingby

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ChildFragmentScoped
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.details.comingby.fragments.ComingByItemListActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class ComingByModule {
    @Binds
    @IntoMap
    @ViewModelKey(ComingByViewModel::class)
    abstract fun bindComingByViewModel(viewModel: ComingByViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ComingByDoctorsViewModel::class)
    abstract fun bindComingByDoctorsViewModel(viewModel: ComingByDoctorsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ComingByTypeViewModel::class)
    abstract fun bindComingByTypeViewModel(viewModel: ComingByTypeViewModel): ViewModel
}