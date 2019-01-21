package com.wxsoft.fcare.di

import com.wxsoft.fcare.core.di.ActivityScoped
import com.wxsoft.fcare.ui.details.checkbody.CheckBodyActivity
import com.wxsoft.fcare.ui.details.checkbody.CheckBodyModule
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseActivity
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseModule
import com.wxsoft.fcare.ui.details.dispatchcar.DispatchCarActivity
import com.wxsoft.fcare.ui.details.dispatchcar.DispatchCarModule
import com.wxsoft.fcare.ui.details.dominating.DoMinaActivity
import com.wxsoft.fcare.ui.details.dominating.DoMinaModule
import com.wxsoft.fcare.ui.details.informedconsent.InformedConsentActivity
import com.wxsoft.fcare.ui.details.informedconsent.InformedConsentModule
import com.wxsoft.fcare.ui.details.informedconsent.addinformed.AddInformedConsentActivity
import com.wxsoft.fcare.ui.details.informedconsent.informeddetails.InformedConsentDetailsActivity
import com.wxsoft.fcare.ui.details.measures.MeasuresActivity
import com.wxsoft.fcare.ui.details.measures.MeasuresModule
import com.wxsoft.fcare.ui.details.medicalhistory.MedicalHistoryActivity
import com.wxsoft.fcare.ui.details.medicalhistory.MedicalHistoryModule
import com.wxsoft.fcare.ui.details.pharmacy.PharmacyActivity
import com.wxsoft.fcare.ui.details.pharmacy.PharmacyModule
import com.wxsoft.fcare.ui.details.thrombolysis.CatheterActivity
import com.wxsoft.fcare.ui.details.thrombolysis.CatheterModule
import com.wxsoft.fcare.ui.details.thrombolysis.ThrombolysisActivity
import com.wxsoft.fcare.ui.details.thrombolysis.ThrombolysisModule
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsModule
import com.wxsoft.fcare.ui.login.LoginActivity
import com.wxsoft.fcare.ui.login.LoginModule
import com.wxsoft.fcare.ui.main.MainActivity
import com.wxsoft.fcare.ui.main.MainModule
import com.wxsoft.fcare.ui.message.MessageActivity
import com.wxsoft.fcare.ui.message.MessageModule
import com.wxsoft.fcare.ui.message.MessageViewModel
import com.wxsoft.fcare.ui.patient.PatientEmrActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.patient.ProfileModule
import com.wxsoft.fcare.ui.rating.RatingActivity
import com.wxsoft.fcare.ui.rating.RatingModule
import com.wxsoft.fcare.ui.rating.RatingSubjectActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module
 * ActivityBindingModule is on, in our case that will be [AppComponent]. You never
 * need to tell [AppComponent] that it is going to have getAll these subcomponents
 * nor do you need to tell these subcomponents that [AppComponent] exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the
 * specified modules and be aware of a scope annotation [@ActivityScoped].
 * When Dagger.Android annotation processor runs it will create 2 subcomponents for us.
 */
@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [LoginModule::class])
    internal abstract fun loginActivity(): LoginActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun mainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [DoMinaModule::class])
    internal abstract fun doMinaActivity(): DoMinaActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [DispatchCarModule::class])
    internal abstract fun dispatchCarActivity(): DispatchCarActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [VitalSignsModule::class])
    internal abstract fun vitalSignsActivity(): VitalSignsActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [CheckBodyModule::class])
    internal abstract fun checkBodyActivity(): CheckBodyActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ProfileModule::class])
    internal abstract fun profileActivity(): ProfileActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MedicalHistoryModule::class])
    internal abstract fun medicalHistoryActivity(): MedicalHistoryActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MeasuresModule::class])
    internal abstract fun medicalMeasuresActivity(): MeasuresActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [PharmacyModule::class])
    internal abstract fun pharmacyActivity(): PharmacyActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [RatingModule::class])
    internal abstract fun ratingActivity(): RatingActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [RatingModule::class])
    internal abstract fun ratingSubjectActivity(): RatingSubjectActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [DiagnoseModule::class])
    internal abstract fun diagnoseActivity(): DiagnoseActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [InformedConsentModule::class])
    internal abstract fun informedConsentActivity(): InformedConsentActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [InformedConsentModule::class])
    internal abstract fun addInformedConsentActivity(): AddInformedConsentActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [InformedConsentModule::class])
    internal abstract fun informedConsentDetailsActivity(): InformedConsentDetailsActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ThrombolysisModule::class])
    internal abstract fun thrombolysisActivity(): ThrombolysisActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ProfileModule::class,DoMinaModule::class])
    internal abstract fun patientEmrActivity(): PatientEmrActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MessageModule::class])
    internal abstract fun messageActivity(): MessageActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [CatheterModule::class])
    internal abstract fun catheterActivity(): CatheterActivity



}