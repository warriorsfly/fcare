package com.wxsoft.fcare.ui.main

import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.FragmentScoped
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.main.fragment.assignment.AssignmentFragment
import com.wxsoft.fcare.ui.main.fragment.assignment.AssignmentViewModel
import com.wxsoft.fcare.ui.main.fragment.profile.UserProfileFragment
import com.wxsoft.fcare.ui.main.fragment.profile.UserProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserProfileViewModel::class)
    abstract fun bindUserProfileViewModel(viewModel: UserProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AssignmentViewModel::class)
    abstract fun bindAssignmentViewModel(viewModel: AssignmentViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeUserProfileFragment(): UserProfileFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeAssignmentFragment(): AssignmentFragment


}