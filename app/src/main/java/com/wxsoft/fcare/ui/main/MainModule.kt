package com.wxsoft.fcare.ui.main

import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.di.ChildFragmentScoped
import com.wxsoft.fcare.di.FragmentScoped
import com.wxsoft.fcare.di.ViewModelKey
import com.wxsoft.fcare.ui.main.dialog.ShowPatientDialog
import com.wxsoft.fcare.ui.main.fragment.patients.PatientsFragment
import com.wxsoft.fcare.ui.main.fragment.patients.PatientsViewModel
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
    @ViewModelKey(PatientsViewModel::class)
    abstract fun bindPatientsViewModel(viewModel: PatientsViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindPatientViewModel(viewModel: MainViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeUserProfileFragment(): UserProfileFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributePatientsFragment(): PatientsFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeShowPatientDialog(): ShowPatientDialog
}