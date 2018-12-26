package com.wxsoft.fcare.ui.detail

import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.di.ChildFragmentScoped
import com.wxsoft.fcare.di.FragmentScoped
import com.wxsoft.fcare.di.ViewModelKey
import com.wxsoft.fcare.ui.detail.dialog.BindRfidDialog
import com.wxsoft.fcare.ui.detail.dialog.Check.CheckDialog
import com.wxsoft.fcare.ui.detail.dialog.VitalSignDialog
import com.wxsoft.fcare.ui.detail.dialog.heart.HeartDialog
import com.wxsoft.fcare.ui.detail.dialog.inital.DiagnosisDialog
import com.wxsoft.fcare.ui.detail.dialog.inital.DiagnosisViewModel
import com.wxsoft.fcare.ui.detail.dialog.notify.NotifyViewModel
import com.wxsoft.fcare.ui.detail.dialog.notify.NotifycationDialog
import com.wxsoft.fcare.ui.detail.dialog.share.ShareDialog
import com.wxsoft.fcare.ui.detail.dialog.share.ShareViewModel
import com.wxsoft.fcare.ui.detail.dialog.transfer.TransferDialog
import com.wxsoft.fcare.ui.detail.dialog.transfer.TransferViewModel
import com.wxsoft.fcare.ui.detail.fragment.conduitroom.ActivateConduitRoomFragment
import com.wxsoft.fcare.ui.detail.fragment.conduitroom.ArriveConduitFragment
import com.wxsoft.fcare.ui.detail.fragment.conduitroom.StartConduitRoomFragment
import com.wxsoft.fcare.ui.detail.fragment.countdown.CountdownTimeFragment
import com.wxsoft.fcare.ui.detail.fragment.detour.DetourFragment
import com.wxsoft.fcare.ui.detail.fragment.diagnosis.DischargeDiagnosisFragment
import com.wxsoft.fcare.ui.detail.fragment.drug.DrugFragment
import com.wxsoft.fcare.ui.detail.fragment.emr.EmrFragment
import com.wxsoft.fcare.ui.detail.fragment.evaluation.EvaluationFragment
import com.wxsoft.fcare.ui.detail.fragment.grace.GraceFragment
import com.wxsoft.fcare.ui.detail.fragment.map.MapFragment
import com.wxsoft.fcare.ui.detail.fragment.outcomes.OutcomesFragment
import com.wxsoft.fcare.ui.detail.fragment.pci.PCIFragment
import com.wxsoft.fcare.ui.detail.fragment.profile.ProfileFragment
import com.wxsoft.fcare.ui.detail.fragment.thrombolysis.PreThrombolysisFragment
import com.wxsoft.fcare.ui.detail.fragment.thrombolysis.WithinThrombolysisFragment
import com.wxsoft.fcare.ui.detail.fragment.timeline.TimeLineFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class PatientDetailModule {

    @Binds
    @IntoMap
    @ViewModelKey(PatientDetailViewModel::class)
    abstract fun bindPatientDetailViewModel(viewModel: PatientDetailViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(NotifyViewModel::class)
    abstract fun bindNotifyViewModel(viewModel: NotifyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DiagnosisViewModel::class)
    abstract fun bindDiagnosisViewModel(viewModel: DiagnosisViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShareViewModel::class)
    abstract fun bindShareViewModel(viewModel: ShareViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(TransferViewModel::class)
    abstract fun bindTransferViewModel(viewModel: TransferViewModel): ViewModel


    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeTimeLineFragment(): TimeLineFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeMapFragment(): MapFragment


    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeEvaluationFragment(): EvaluationFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeProfileFragment(): ProfileFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeDrugFragment(): DrugFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributePreThrombolysisFragment(): PreThrombolysisFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeWithinThrombolysisFragment(): WithinThrombolysisFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeGraceFragment(): GraceFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeDetourFragment(): DetourFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributePCIFragment(): PCIFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeStartConduitRoomFragment(): StartConduitRoomFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeActivateConduitRoomFragment(): ActivateConduitRoomFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeArriveConduitFragmentFragment(): ArriveConduitFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeDischargeDiagnosisFragment(): DischargeDiagnosisFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeOutcomesFragment(): OutcomesFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeCountdownFragment(): CountdownTimeFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeEmrFragment(): EmrFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeBindRfidDialog(): BindRfidDialog

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeVitalSignDialog(): VitalSignDialog

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeNotifycationDialog(): NotifycationDialog

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeHeartDialog(): HeartDialog

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeCheckDialog(): CheckDialog

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeDiagnosisDialog(): DiagnosisDialog

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeShareDialog(): ShareDialog

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeTransferDialog(): TransferDialog

}