package com.wxsoft.fcare.di

import com.wxsoft.fcare.core.di.ActivityScoped
import com.wxsoft.fcare.ui.ViewPoolModule
import com.wxsoft.fcare.ui.details.assistant.AssistantExaminationActivity
import com.wxsoft.fcare.ui.details.assistant.AssistantExaminationModule
import com.wxsoft.fcare.ui.details.assistant.troponin.JGDBActivity
import com.wxsoft.fcare.ui.details.catheter.CatheterActivity
import com.wxsoft.fcare.ui.details.catheter.CatheterModule
import com.wxsoft.fcare.ui.details.checkbody.CheckBodyActivity
import com.wxsoft.fcare.ui.details.checkbody.CheckBodyModule
import com.wxsoft.fcare.ui.details.checkbody.select.SelectBodyItemsActivity
import com.wxsoft.fcare.ui.details.checkbody.select.SelectBodyItemsModule
import com.wxsoft.fcare.ui.details.complaints.ComplaintsActivity
import com.wxsoft.fcare.ui.details.complaints.ComplaintsModule
import com.wxsoft.fcare.ui.details.complication.ComplicationActivity
import com.wxsoft.fcare.ui.details.complication.ComplicationModule
import com.wxsoft.fcare.ui.details.ct.CTActivity
import com.wxsoft.fcare.ui.details.ct.CTModule
import com.wxsoft.fcare.ui.details.cure.CureActivity
import com.wxsoft.fcare.ui.details.cure.CureModule
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseActivity
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseModule
import com.wxsoft.fcare.ui.details.diagnose.diagnosenew.DiagnoseNewActivity
import com.wxsoft.fcare.ui.details.diagnose.diagnosenew.drug.ACSDrugActivity
import com.wxsoft.fcare.ui.details.diagnose.diagnosenew.treatment.TreatmentOptionsActivity
import com.wxsoft.fcare.ui.details.diagnose.record.DiagnoseRecordActivity
import com.wxsoft.fcare.ui.details.diagnose.record.DiagnoseRecordModule
import com.wxsoft.fcare.ui.details.diagnose.select.SelectDiagnoseActivity
import com.wxsoft.fcare.ui.details.diagnose.select.SelectDiagnoseModule
import com.wxsoft.fcare.ui.details.dispatchcar.DispatchCarActivity
import com.wxsoft.fcare.ui.details.dispatchcar.DispatchCarModule
import com.wxsoft.fcare.ui.details.dominating.DoMinaActivity
import com.wxsoft.fcare.ui.details.dominating.DoMinaModule
import com.wxsoft.fcare.ui.details.ecg.EcgActivity
import com.wxsoft.fcare.ui.details.ecg.EcgModule
import com.wxsoft.fcare.ui.details.ecg.ReactiveEcgActivity
import com.wxsoft.fcare.ui.details.fast.FastActivity
import com.wxsoft.fcare.ui.details.informedconsent.InformedConsentActivity
import com.wxsoft.fcare.ui.details.informedconsent.InformedConsentModule
import com.wxsoft.fcare.ui.details.informedconsent.addinformed.AddInformedActivity
import com.wxsoft.fcare.ui.details.informedconsent.informeddetails.InformedConsentDetailsActivity
import com.wxsoft.fcare.ui.details.measures.MeasuresActivity
import com.wxsoft.fcare.ui.details.measures.MeasuresModule
import com.wxsoft.fcare.ui.details.medicalhistory.MedicalHistoryActivity
import com.wxsoft.fcare.ui.details.medicalhistory.MedicalHistoryModule
import com.wxsoft.fcare.ui.details.notification.NotificationActivity
import com.wxsoft.fcare.ui.details.notification.NotificationModule
import com.wxsoft.fcare.ui.details.pharmacy.PharmacyActivity
import com.wxsoft.fcare.ui.details.pharmacy.PharmacyModule
import com.wxsoft.fcare.ui.details.pharmacy.drugcar.DrugCarActivity
import com.wxsoft.fcare.ui.details.pharmacy.drugcar.DrugCarModule
import com.wxsoft.fcare.ui.details.pharmacy.drugrecords.DrugRecordsActivity
import com.wxsoft.fcare.ui.details.pharmacy.drugrecords.DrugRecordsModule
import com.wxsoft.fcare.ui.details.pharmacy.selectdrugs.SelectDrugsActivity
import com.wxsoft.fcare.ui.details.pharmacy.selectdrugs.SelectDrugsModule
import com.wxsoft.fcare.ui.details.reperfusion.ReperfusionActivity
import com.wxsoft.fcare.ui.details.reperfusion.ReperfusionModule
import com.wxsoft.fcare.ui.details.strategy.StrategyActivity
import com.wxsoft.fcare.ui.details.strategy.StrategyModule
import com.wxsoft.fcare.ui.details.thrombolysis.ThrombolysisActivity
import com.wxsoft.fcare.ui.details.thrombolysis.ThrombolysisModule
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsModule
import com.wxsoft.fcare.ui.details.vitalsigns.records.VitalSignsRecordActivity
import com.wxsoft.fcare.ui.details.vitalsigns.records.VitalSignsRecordModule
import com.wxsoft.fcare.ui.discharge.DisChargeActivity
import com.wxsoft.fcare.ui.discharge.DisChargeModule
import com.wxsoft.fcare.ui.emr.EmrActivity
import com.wxsoft.fcare.ui.launch.LaunchModule
import com.wxsoft.fcare.ui.launch.LauncherActivity
import com.wxsoft.fcare.ui.login.LoginActivity
import com.wxsoft.fcare.ui.login.LoginModule
import com.wxsoft.fcare.ui.main.MainActivity
import com.wxsoft.fcare.ui.main.MainModule
import com.wxsoft.fcare.ui.main.fragment.patients.searchpatients.SearchPatientsActivity
import com.wxsoft.fcare.ui.main.fragment.patients.searchpatients.SearchPatientsModule
import com.wxsoft.fcare.ui.main.fragment.task.searchtask.SearchTaskActivity
import com.wxsoft.fcare.ui.main.fragment.task.searchtask.SearchTaskModule
import com.wxsoft.fcare.ui.message.MessageActivity
import com.wxsoft.fcare.ui.message.MessageModule
import com.wxsoft.fcare.ui.outcome.OutComeActivity
import com.wxsoft.fcare.ui.outcome.OutComeModule
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.patient.ProfileModule
import com.wxsoft.fcare.ui.rating.RatingActivity
import com.wxsoft.fcare.ui.rating.RatingModule
import com.wxsoft.fcare.ui.rating.RatingSubjectActivity
import com.wxsoft.fcare.ui.selecter.SelecterOfOneModelActivity
import com.wxsoft.fcare.ui.selecter.SelecterOfOneModule
import com.wxsoft.fcare.ui.share.ShareActivity
import com.wxsoft.fcare.ui.share.ShareItemListActivity
import com.wxsoft.fcare.ui.share.ShareModule
import com.wxsoft.fcare.ui.workspace.TimePointActivity
import com.wxsoft.fcare.ui.workspace.WorkSpaceModule
import com.wxsoft.fcare.ui.workspace.WorkingActivity
import com.wxsoft.fcare.ui.workspace.addpoint.AddTimeLinePointActivity
import com.wxsoft.fcare.ui.workspace.notify.OneTouchCallingActivity
import com.wxsoft.fcare.ui.workspace.notify.OneTouchCallingModule
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
    @ContributesAndroidInjector(modules = [LaunchModule::class])
    internal abstract fun launcherActivity(): LauncherActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [EcgModule::class])
    internal abstract fun ecgActivity(): EcgActivity

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
    @ContributesAndroidInjector(modules = [SelectBodyItemsModule::class])
    internal abstract fun selectBodyItemsActivity(): SelectBodyItemsActivity

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
    @ContributesAndroidInjector(modules = [RatingModule::class,ViewPoolModule::class])
    internal abstract fun ratingActivity(): RatingActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [RatingModule::class,ViewPoolModule::class])
    internal abstract fun ratingSubjectActivity(): RatingSubjectActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [DiagnoseModule::class])
    internal abstract fun diagnoseActivity(): DiagnoseActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [InformedConsentModule::class])
    internal abstract fun informedConsentActivity(): InformedConsentActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [InformedConsentModule::class])
    internal abstract fun addInformedActivity(): AddInformedActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [InformedConsentModule::class])
    internal abstract fun informedConsentDetailsActivity(): InformedConsentDetailsActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ThrombolysisModule::class])
    internal abstract fun thrombolysisActivity(): ThrombolysisActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MessageModule::class])
    internal abstract fun messageActivity(): MessageActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [CatheterModule::class])
    internal abstract fun catheterActivity(): CatheterActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [CTModule::class])
    internal abstract fun ctActivity(): CTActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [DisChargeModule::class])
    internal abstract fun dischargeActivity(): DisChargeActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [AssistantExaminationModule::class])
    internal abstract fun assistantExaminationActivity(): AssistantExaminationActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [OutComeModule::class])
    internal abstract fun outComeActivity(): OutComeActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ReperfusionModule::class])
    internal abstract fun reperfusionActivity(): ReperfusionActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ComplaintsModule::class])
    internal abstract fun complaintsActivity(): ComplaintsActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [StrategyModule::class])
    internal abstract fun strategyActivity(): StrategyActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [WorkSpaceModule::class,ViewPoolModule::class,RatingModule::class])
    internal abstract fun workingActivity(): WorkingActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [DrugRecordsModule::class])
    internal abstract fun drugRecordsActivity(): DrugRecordsActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [DrugCarModule::class])
    internal abstract fun drugCarActivity(): DrugCarActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SelectDrugsModule::class])
    internal abstract fun selectDrugsActivity(): SelectDrugsActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [VitalSignsRecordModule::class])
    internal abstract fun vitalSignsRecordActivity(): VitalSignsRecordActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [WorkSpaceModule::class])
    internal abstract fun timePointActivity(): TimePointActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [WorkSpaceModule::class])
    internal abstract fun addTimeLinePointActivity(): AddTimeLinePointActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [DiagnoseRecordModule::class])
    internal abstract fun diagnoseRecordActivity(): DiagnoseRecordActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SearchPatientsModule::class])
    internal abstract fun searchPatientsActivity(): SearchPatientsActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ComplicationModule::class])
    internal abstract fun complicationActivity(): ComplicationActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [NotificationModule::class])
    internal abstract fun notificationActivity(): NotificationActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SearchTaskModule::class])
    internal abstract fun searchTaskActivity(): SearchTaskActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SelectDiagnoseModule::class])
    internal abstract fun selectDiagnoseActivity(): SelectDiagnoseActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SelecterOfOneModule::class])
    internal abstract fun selecterOfOneModelActivity(): SelecterOfOneModelActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ShareModule::class])
    internal abstract fun shareActivity(): ShareActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ShareModule::class])
    internal abstract fun shareItemListActivity(): ShareItemListActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [DiagnoseModule::class])
    internal abstract fun diagnoseNewActivity(): DiagnoseNewActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [DiagnoseModule::class])
    internal abstract fun treatmentOptionsActivity(): TreatmentOptionsActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [DiagnoseModule::class])
    internal abstract fun aCSDrugActivity(): ACSDrugActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [])
    internal abstract fun reactiveEcgActivity(): ReactiveEcgActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [WorkSpaceModule::class])
    internal abstract fun emrActivity(): EmrActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [CureModule::class])
    internal abstract fun cureActivity(): CureActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [OneTouchCallingModule::class])
    internal abstract fun oneTouchCallingActivity(): OneTouchCallingActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [AssistantExaminationModule::class])
    internal abstract fun jGDBActivity(): JGDBActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [])
    internal abstract fun fastActivity(): FastActivity


}