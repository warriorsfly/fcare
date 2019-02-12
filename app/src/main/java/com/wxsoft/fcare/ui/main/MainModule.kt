package com.wxsoft.fcare.ui.main

import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.FragmentScoped
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.main.fragment.patients.PatientsFragment
import com.wxsoft.fcare.ui.main.fragment.patients.PatientsViewModel
import com.wxsoft.fcare.ui.main.fragment.task.TaskFragment
import com.wxsoft.fcare.ui.main.fragment.task.TaskViewModel
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

//    @Binds
//    @IntoMap
//    @ViewModelKey(MainViewModel::class)
//    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PatientsViewModel::class)
    abstract fun bindPatientsViewModel(viewModel: PatientsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskViewModel::class)
    abstract fun bindTaskViewModel(viewModel: TaskViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeUserProfileFragment(): UserProfileFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeTaskFragment(): TaskFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributePatientsFragment(): PatientsFragment


}