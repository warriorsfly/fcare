package com.wxsoft.fcare.ui.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import com.google.gson.Gson
import com.wxsoft.fcare.data.*
import com.wxsoft.fcare.data.entity.*
import com.wxsoft.fcare.data.entity.chest.AssistCheck
import com.wxsoft.fcare.data.entity.chest.Evaluation
import com.wxsoft.fcare.data.entity.chest.VitalSign
import com.wxsoft.fcare.data.local.dao.PatientDao
import com.wxsoft.fcare.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.data.remote.*
import com.wxsoft.fcare.result.Event
import com.wxsoft.fcare.result.Resource
import com.wxsoft.fcare.result.succeeded
import com.wxsoft.fcare.utils.DateTimeUtils
import com.wxsoft.fcare.utils.getAfromB
import com.wxsoft.fcare.utils.map
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class PatientDetailViewModel @Inject constructor(
    gson:Gson,
    sharedPreferenceStorage: SharedPreferenceStorage,
    private val patientApi: PatientApi,
    private val drugApi: DrugApi,
    private val thromApi: ThromApi,
    private val graceApi: GraceApi,
    private val pciApi: PciApi,
    private val checkApi: AssistCheckApi,
    private val outComeApi: OutComeApi,
    private val detourApi: DetourApi,
    private val outHospitalApi: OutHospitalApi,
    private val dictEnumApi: DictEnumApi,
    private val evaluationApi: EvaluationApi,
    private val vitalSignApi: VitalSignApi,
    private val operationApi: OperationMenuApi,
    private val emrLogApi: EmrLogApi,
    private val gpsApi: GpsApi,
    private val dao: PatientDao): ViewModel(), EventActions {

    val account: Account

    private var _attackTime: Long = 0

    private var timer:Disposable? = null

    private val _navigateToOperationAction = MutableLiveData<Event<String>>()
    private val _errorToOperationAction = MutableLiveData<Event<String>>()

    /**
     * ClickActions
     */
    val navigateToOperationAction: LiveData<Event<String>>
        get() = _navigateToOperationAction


    val navigateToErrorAction: LiveData<Event<String>>
        get() = _errorToOperationAction

    override fun onOpen(id: String) {

        _navigateToOperationAction.value = Event(id)
    }

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            field = value
            if (value == "") return
            onSwipeRefresh()
        }

    /**
     * 是否正在加载数据
     */
    val isLoading: LiveData<Boolean>

    /**
     * 病人信息
     */
    val patient: LiveData<Patient>

    val tabIndex = ObservableInt(0)

    val menuShowing = ObservableBoolean(false)
    val refreshable: LiveData<Boolean>
    /**
     *患者给药信息
     */
    val drug: LiveData<Drug>
    val otherDrugs: LiveData<List<AnticoagulationDrug>>
    val doctors: LiveData<List<Doctor>>
    val interventionPerson: LiveData<List<Doctor>>

    /**
     *院前溶栓处置
     */
    val thrombolysis: LiveData<Thrombolysis>
    /**
     *辅助检查
     */
    val assistCheck: LiveData<AssistCheck>

    /*
    * Grace评分
    */
    val grace: LiveData<Grace>
    /*
    * 出院转归
    */
    val outCome: LiveData<OutCome>
    /**
     * 肌酐蛋白单位
     */
    val troponinUnitsItems: LiveData<List<String>>
    private lateinit var troponinUnits: List<Dictionary>
//    val troponinCharacter : List<String> = listOf("","")
    /*
   * 出院诊断
   */
    val outHospitalDiagnosis: LiveData<OutHospitalDiagnosis>
    /**
     *绕行
     */
    val detour: LiveData<Detour>
    val pci: LiveData<Pci>
    /**
     * 病人病情评估
     */
    val dictionary: LiveData<List<Dictionary>>
    /**
     * 菜单数据
     */
    val menuItems: LiveData<List<OperationMenu>>

    val vital: LiveData<VitalSign>
    val savablevaluation: LiveData<Boolean>
    val emrLogs: LiveData<List<EmrLog>>
    val consciousnessItems: LiveData<List<String>>
    private lateinit var consciousItems: List<Dictionary>
    val selectedConsciousnessPosition: ObservableInt = ObservableInt(0)

    /**
     *获取病人信息
     */
    private val loadPatientResult = MediatorLiveData<Resource<Patient>>()
    //给药
    private val loadDrugResult = MediatorLiveData<Resource<Response<Drug>>>()
    //院前溶栓处置
    private val loadThrombolysisResult = MediatorLiveData<Resource<Response<Thrombolysis>>>()
    private val loadGraceResult = MediatorLiveData<Resource<Response<Grace>>>()
    private val loadAssistCheck = MediatorLiveData<Resource<Response<AssistCheck>>>()
    private val loadPciResult = MediatorLiveData<Resource<Response<Pci>>>()
    private val loadOtherDrugs = MediatorLiveData<Resource<List<AnticoagulationDrug>>>()
    private val loadOutCome = MediatorLiveData<Resource<Response<OutCome>>>()
    private val loadDetour = MediatorLiveData<Resource<Response<Detour>>>()
    private val loadOutHospitalDiagnosis = MediatorLiveData<Resource<Response<OutHospitalDiagnosis>>>()
    private val loadDoctors = MediatorLiveData<Resource<List<Doctor>>>()
    private val loadinterventionPersons = MediatorLiveData<Resource<List<Doctor>>>()
    private val loadVitalDetail = MediatorLiveData<Resource<VitalSign>>()

    private val bindRfidResult = MediatorLiveData<Resource<String>>()
    private val evaluationSavableResult = MediatorLiveData<Boolean>()

    /**
     *获取肌酐蛋白单位字典
     */
    private val loadTroponinDictEnumResult = MediatorLiveData<Resource<List<Dictionary>>>()

    /**
     *获取病情评估字典
     */
    private val loadEvaluationDictEnumResult = MediatorLiveData<Resource<List<Dictionary>>>()

    /**
     * 获取菜单
     */
    private val loadMenuResult = MediatorLiveData<Resource<List<OperationMenu>>>()
    private val loadVitalResult = MediatorLiveData<Resource<VitalSign>>()

    /**
     * tab切换以及菜单弹出
     */
    private val refreshableResult = MediatorLiveData<Boolean>()

    private val loadEmrLogResult = MediatorLiveData<Resource<List<EmrLog>>>()
    /**
     * 意识字典
     */
    private val loadConsciousnessResult = MediatorLiveData<Resource<List<Dictionary>>>()

    init {

        val s = sharedPreferenceStorage.userInfo!!
        account = gson.fromJson(s, Account::class.java)

        onSwipeRefresh()
        isLoading = loadPatientResult.map { it == Resource.Loading }

        refreshable = refreshableResult.map { it }
        patient = loadPatientResult.map { (it as? Resource.Success)?.data ?: Patient("") }

        drug = loadDrugResult.map {
            var re = (it as? Resource.Success)?.data

            val d = if (re != null && re.success) {
                if (re.result == null) Drug("") else re.result
            } else Drug("")
            d?.patientId = patientId
            d?.setUpChecked()
            return@map d!!
        }

        dictionary = loadEvaluationDictEnumResult.map { (it as? Resource.Success)?.data ?: emptyList() }

        menuItems = loadMenuResult.map { (it as?  Resource.Success)?.data ?: emptyList() }
        emrLogs = loadEmrLogResult.map { (it as?  Resource.Success)?.data ?: emptyList() }

        vital = loadVitalResult.map { (it as? Resource.Success)?.data ?: VitalSign("") }


        savablevaluation = evaluationSavableResult.map { it }
        consciousnessItems = loadConsciousnessResult.map {

            val cos=(it as? Resource.Success)?.data?: emptyList()
            consciousItems=cos
            return@map consciousItems.map { item -> item.itemName }

        }
        troponinUnitsItems= loadTroponinDictEnumResult.map {
            val cos=(it as? Resource.Success)?.data?: emptyList()
            troponinUnits=cos
            return@map troponinUnits.map { item -> item.itemName }
        }

        refreshableResult.value = true

        detour = loadDetour.map {
            var re = (it as? Resource.Success)?.data

            val d = if (re != null && re.success) {
                if (re.result == null) Detour("") else re.result
            } else Detour("")
            d?.patientId = patientId
            d?.createrId = account.id
            d?.setUpClick()
            return@map d!!
        }
        pci = loadPciResult.map {
            var re = (it as? Resource.Success)?.data

            val d = if (re != null && re.success) {
                if (re.result == null) Pci("") else re.result
            } else Pci("")
            d?.patientId = patientId
            return@map d!!
        }
        thrombolysis = loadThrombolysisResult.map {
            var re = (it as? Resource.Success)?.data
            val d = if (re != null && re.success) {
                if (re.result == null) Thrombolysis("") else re.result
            } else Thrombolysis("")
            d?.patientId = patientId
            d?.setUpChecked()
            return@map d!!
        }
        grace = loadGraceResult.map {
            var re = (it as? Resource.Success)?.data
            val d = if (re != null && re.success) {
                if (re.result == null) Grace("") else re.result
            } else Grace("")
            d?.patientId = patientId
            d?.setCheckedValue()
            return@map d!!
        }
        outCome = loadOutCome.map {
            var re = (it as? Resource.Success)?.data
            val d = if (re != null && re.success) {
                if (re.result == null) OutCome("") else re.result
            } else OutCome("")
            d?.patientId = patientId
            d?.setUpChecked()
            return@map d!!
        }
        assistCheck = loadAssistCheck.map {
            var re = (it as? Resource.Success)?.data
            val d = if (re != null && re.success) {
                if (re.result == null) AssistCheck("") else re.result
            } else AssistCheck("")
            d?.patientId = patientId
            d?.setUpChecked()
            return@map d!!
        }
        outHospitalDiagnosis = loadOutHospitalDiagnosis.map {
            var re = (it as? Resource.Success)?.data
            val d = if (re != null && re.success) {
                if (re.result == null) OutHospitalDiagnosis("") else re.result
            } else OutHospitalDiagnosis("")
            d?.patientId = patientId
            d?.createrId = patientId
            d?.setUpChecked()
            return@map d!!
        }
        otherDrugs = loadOtherDrugs.map { (it as? Resource.Success)?.data ?: emptyList() }
        doctors = loadDoctors.map { (it as? Resource.Success)?.data ?: emptyList() }
        interventionPerson = loadinterventionPersons.map { (it as? Resource.Success)?.data ?: emptyList() }


    }

    fun changeTab(index: Int) {
        tabIndex.set(index)
        refreshableResult.value = tabIndex.get() == 0 && !menuShowing.get()
    }

    fun menuShown(showing: Boolean) {
        menuShowing.set(showing)
        refreshableResult.value = tabIndex.get() == 0 && !menuShowing.get()
    }

    /**
     * Swipe事件
     */
    fun onSwipeRefresh() {

        if(timer?.isDisposed == false)
            timer?.dispose()
        if (patientId == "") {
            var newPatient = Patient("")
            newPatient.createrId = account.id
            newPatient.createrName = account.userName

            loadPatientResult.value = Resource.Success(newPatient)
        } else {
            loadPatientDetail()
            loadEmrLog()
            getDoctors()
            getinterventionPersons()
        }


    }


    /**
     * 获取病人病例
     */
    private fun loadPatientDetail() {

        object : NetSingleResource<Patient, Response<Patient>>() {
            override fun saveCallResult(request: Response<Patient>) {

                if (request.success && request.result != null) {
                    dao.insert(request.result!!)
                }
            }

            override fun loadFromDb(): Flowable<Patient> {
                return dao.getOne(patientId)
            }

            override fun createCall(): Single<Response<Patient>> {
                return patientApi.getOne(patientId)
            }

            override fun shouldFetch(): Boolean {
                return true
            }

        }.data.subscribe {
            loadPatientResult.value = it

            when (it) {
                is Resource.Success -> {
                    var p = it.data

                    if (p.id.isNotEmpty()) {
                        loadMenu(p.id)
                        loadEvaluations(p)
//                        loadVitalSign()
                    }
                    if (p.attack_Time?.isNotEmpty() ?: false) {
                        _attackTime = DateTimeUtils.formatter.parse(p.attack_Time?.replace("T", " ")).time
//                        handle.postDelayed(runnable, 1000)

                        if (timer?.isDisposed != false) {
                            timer = Observable.interval(1, TimeUnit.SECONDS).subscribe {
                                if (patient.value != null || patient.value!!.attack_Time?.isEmpty() ?: true)
                                    patient.value?.attack_Time?.isNotEmpty().let {
                                        patient.value?.attackClock =
                                                getAfromB(_attackTime, System.currentTimeMillis())
                                    }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
        }
    }


    /**
     * 保存病人基本系信息
     */
    fun savePatientInfo() {
        
        if (patient.value!!.name.isNotEmpty()) {
            patientApi.save(patient.value!!).toResource()

                .subscribe {
                    patientId = (it as? Resource.Success)?.data ?: ""
                    if(it is Resource.Error) {
                        _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                    }
                }

            
        }

    }

    /**
     * 加载菜单
     */
    private fun loadMenu(pid: String) {



        operationApi.getMenuByUserAndPatient(account.id, pid).toResource()

            .subscribe {
                loadMenuResult.value = it
                if (it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }

        
    }

    /**
     * 获取评估字典信息（字典里含有病人并不具备的评估 选项
     */
    private fun loadEvaluations(p: Patient) {

        dictEnumApi.loadDictEvaluations().toResource()
            .subscribe {
                loadEvaluationDictEnumResult.value = it
                if ((p.evaluations!=null && p.evaluations.size >0) && it.succeeded) {
                    var evaluations = (it as Resource.Success).data
                    for (ev in evaluations) {
                        ev.checked = p.evaluations.any { it.code == ev.itemCode }
                    }
                }
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }

        
    }

    /**
     * 获取肌酐蛋白单位字典信息
     */
    fun loadTroponin() {

        dictEnumApi.loadTroponinUnit().toResource()
            .subscribe {
                loadTroponinDictEnumResult.value = it
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }


    }

    fun saveEvaluations() {

        val evaluations = dictionary.value!!.filter { it.checked }
            .map { Evaluation("", it.itemCode, it.itemName, patientId, account.id) }
        evaluationApi.addEvaluation(evaluations).toResource()
            .subscribe {
                when (it) {

                    is Resource.Success -> {

                        patient.value?.evaluations=evaluations
                        for (ev in dictionary.value!!) {
                            ev.checked = patient.value?.evaluations?.any { it.code == ev.itemCode }?:false
                        }
                    }
                    is Resource.Error -> {
                        _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                    }
                }
            }

        
    }

    fun saveVitalSign() {

        val v = vital.value!!
        if (v.id.isEmpty())
            v.patientId = patientId
        if (
            v.blood_Pressure.isEmpty() ||
//            v.body_Temperature==0f||
            v.heart_Rate == 0
        ) return
        if (selectedConsciousnessPosition.get() == -1) return
        v.consciousness_Type = consciousItems.get(selectedConsciousnessPosition.get()).itemCode
        (if (v.id.isEmpty()) vitalSignApi.insert(v) else vitalSignApi.update(v)).toResource()
            .subscribe { }

        
    }

    //TODO 需要确认contractMap
    fun loadVitalSign() {
        dictEnumApi.loadConsciousness().toResource()
            .doOnSuccess {  loadConsciousnessResult.value = it}
            .flatMap {vitalSignApi.list(patientId).toResource()  }
            .subscribe {vi->

                when (vi) {
                            is Resource.Success -> {
                                val l = (vi as? Resource.Success)?.data ?: emptyList()

                                if (l.isEmpty()) {
                                    loadVitalResult.value = Resource.Success(VitalSign(""))
                                } else {
                                    loadVitalResult.value = Resource.Success(l[0])

                                    selectedConsciousnessPosition.set(consciousnessItems.value?.indexOf(l[0].consciousness_Type)?:-1)
                                }

                            }
                            is Resource.Loading -> {
                                loadVitalResult.value = Resource.Loading
                            }
                            is Resource.Error -> {
                                loadVitalResult.value = vi
                                _errorToOperationAction.value = Event(vi.throwable.message ?: "错误")
                            }
                        }
            }
    }



    fun bindRfid(id:String) {
        patientApi.bindRfid(patientId, id).toWxResource()
            .doOnNext { bindRfidResult.value = it }
            .subscribe{ result ->
                when (result ) {
                    is Resource.Success->
                        patient.value?.wristband_Number = result.data

                    is Resource.Error-> {
                        _errorToOperationAction.value = Event(result.throwable.message ?: "错误")
                    }
                }
            }
        
    }

    fun uploadGpsLocation(location: GpsLocation){
        gpsApi.save(location).toResource()
            .subscribe { }

        
    }

    fun checkEvaSavable(){
        if(dictionary.value==null) {
            evaluationSavableResult.value = false
        }else {
            evaluationSavableResult.value = dictionary.value!!.any { it.checked }
        }
    }

    fun loadEmrLog(){
        emrLogApi.getEmrLog(patientId).toResource().subscribe {
            loadEmrLogResult.value=it
            if(it is Resource.Error) {
                _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
            }
        }

        
    }


    /**
     * 获取ACS给药信息
     */
    fun loadDrugDetail(){
        drugApi.getAcs(patientId).toResource()
            .subscribe{
                loadDrugResult.value = it
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
        
    }
    //获取首次给药的其他药物
    fun loadOrthDrugs(){
         drugApi.getOtherDrugs("22").toResource()
            .subscribe {
                loadOtherDrugs.value = it
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
        
    }

    fun saveAcs(ace:Drug){
         drugApi.save(ace).toWxResource()
            .subscribe{
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
        
    }
    //获取决定医生
    fun getDoctors(){
         pciApi.getDoctors(account.id).toWxResource()
            .subscribe {
                loadDoctors.value = it
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }
    //获取介乎人员
    fun getinterventionPersons(){
        pciApi.getInterventionPersons(account.id).toWxResource()
            .subscribe{
                loadinterventionPersons.value = it
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }

    fun loadPci(){
        pciApi.getPci(patientId).toResource()
            .subscribe{
                loadPciResult.value = it
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }

    fun savePci(pci:Pci){
        pciApi.save(pci).toResource()
            .subscribe{

                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }
    //启动导管室
    fun statrConduitTime(mtime : String){
        pciApi.statrConduitTime(patientId,account.id,mtime).toResource()
            .subscribe{
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }
    //激活导管室
    fun activateConduitTime(mtime : String){
        pciApi.activateConduitTime(patientId,account.id,mtime).toResource()
            .subscribe{
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }
    //到达导管室
    fun arriveConduitTime(mtime : String){
        pciApi.arriveConduitTime(patientId,account.id,mtime).toResource()
            .subscribe{
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }


    //获取溶栓信息
    fun getThrom(isPre:Boolean){
        thromApi.getThrombolysis(patientId,isPre).toResource()
            .subscribe {
                loadThrombolysisResult.value = it
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }
    fun saveThrom(throm:Thrombolysis){
        thromApi.save(throm).toResource()
            .subscribe {
            }
    }

    //获取grace信息
    fun getgrace(){
        graceApi.getGrace(patientId).toResource()
            .subscribe {
                loadGraceResult.value = it
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }
    fun saveGrace(grace:Grace){
        graceApi.save(grace).toWxResource()
            .subscribe {
            }
    }
    //获取出院转归信息
    fun getOutCome(){
        outComeApi.getOutcome(patientId).toResource()
            .subscribe {
                loadOutCome.value = it
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }
    fun saveOutCome(outcome:OutCome){
        outComeApi.save(outcome).toResource()
            .subscribe{
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }

    //获取出院诊断信息
    fun getOutHospitalDiagnosis(){
        outHospitalApi.getOutcome(patientId).toResource()
            .subscribe {
                loadOutHospitalDiagnosis.value = it
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }
    fun saveOutHospitalDiagnosis(outHospitalDiagnosis: OutHospitalDiagnosis){
        outHospitalApi.save(outHospitalDiagnosis).toResource()
            .subscribe{
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }


    override fun onCleared() {
        super.onCleared()
        timer?.dispose()

    }

    fun getAssistCheck(){
        checkApi.getAssistCheck(patientId).toResource()
                .subscribe{
                    loadAssistCheck.value = it
                    if(it is Resource.Error) {
                        _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                    }
                }
    }

    fun saveAssistCheck(check : AssistCheck){
        checkApi.insert(check).toResource()
            .subscribe {
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }

    fun getDetour(){
        detourApi.getDetour(patientId).toResource()
            .subscribe{
                loadDetour.value = it
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }

    fun saveDetour(detour: Detour){
        detourApi.save(detour).toResource()
            .subscribe{
                if(it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }
    }


}