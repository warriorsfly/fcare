package com.wxsoft.fcare.ui.detail.dialog.share

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import cn.jiguang.share.android.api.JShareInterface
import cn.jiguang.share.android.api.PlatActionListener
import cn.jiguang.share.android.api.Platform
import cn.jiguang.share.android.api.ShareParams
import cn.jiguang.share.wechat.Wechat
import com.google.gson.Gson
import com.wxsoft.fcare.data.entity.Account
import com.wxsoft.fcare.data.entity.Response
import com.wxsoft.fcare.data.entity.chest.Evaluation
import com.wxsoft.fcare.data.entity.chest.InitialDiagnosis
import com.wxsoft.fcare.data.entity.chest.VitalSign
import com.wxsoft.fcare.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.data.remote.EvaluationApi
import com.wxsoft.fcare.data.remote.InitialDiagnosisApi
import com.wxsoft.fcare.data.remote.VitalSignApi
import com.wxsoft.fcare.data.toResource
import com.wxsoft.fcare.result.Resource
import com.wxsoft.fcare.utils.map
import java.util.HashMap
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.emptyList
import kotlin.collections.toTypedArray

class ShareViewModel @Inject constructor(private val evaluationApi: EvaluationApi,
                                         private val vitalSignApi: VitalSignApi,
                                         private val diagnosisApi: InitialDiagnosisApi,
                                         gson: Gson,
                                         sharedPreferenceStorage: SharedPreferenceStorage): ViewModel() {

    private val account: Account
    var patientId: String = ""
        set(value) {
            field = value
            loadVital()
            loadEvaluations()
            loadDiagnosis()
        }

    val files=Array<String?>(3){null}
    val showVitalImage=ObservableBoolean(false)
    val showDiagnosisImage=ObservableBoolean(false)
    val showEvaluationImage=ObservableBoolean(false)
    val evaluations:LiveData<List<Evaluation>>
    private val loadEvaluationResult=MediatorLiveData<Resource<List<Evaluation>>>()

    val vital:LiveData<VitalSign?>
    private val loadVitalResult=MediatorLiveData<Resource<VitalSign>>()

    val diagnosis:LiveData<Response<InitialDiagnosis?>>
    private val loadDiagnosisResult=MediatorLiveData<Resource<Response<InitialDiagnosis?>>>()
    private val shareListener: PlatActionListener
    init {
        val userInfo = sharedPreferenceStorage.userInfo!!
        account = gson.fromJson(userInfo, Account::class.java)

        evaluations=loadEvaluationResult.map { (it as? Resource.Success)?.data?:emptyList() }
        vital=loadVitalResult.map { (it as? Resource.Success)?.data }
        diagnosis=loadDiagnosisResult.map { (it as? Resource.Success)?.data?:Response(false)}

        shareListener = object : PlatActionListener {
            override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) {

            }

            override fun onCancel(p0: Platform?, p1: Int) {

            }

            override fun onError(p0: Platform?, p1: Int, p2: Int, p3: Throwable?) {

            }
        }

    }

    private fun loadEvaluations(){
        evaluationApi.loadPatientEvaluations(patientId).toResource().subscribe{loadEvaluationResult.value=it}
    }

    @SuppressLint("CheckResult")
    private fun loadVital(){
        vitalSignApi.list(patientId).toResource().subscribe{
            when(it){
                is Resource.Success->{
                    loadVitalResult.value = Resource.Success(it.data?.get(0))
                }
                is Resource.Error->{
                    loadVitalResult.value = it
                }
            }
        }
    }

    private fun loadDiagnosis(){
        diagnosisApi.getDiagnosis(patientId).toResource().subscribe{

            loadDiagnosisResult.value =it
        }
    }

    fun share(){
        if(JShareInterface.isSupportAuthorize(Wechat.Name)  &&(showVitalImage.get()||showDiagnosisImage.get()||showEvaluationImage.get())){

            val params = ShareParams()
//            params.title = account.userName
            params.text = account.hospitalName
            val shareArray=ArrayList<String>()
            if(showEvaluationImage.get() && files[0]!=null){
                shareArray.add(files[0]!!)
            }
            if(showVitalImage.get() && files[1]!=null){
                shareArray.add(files[1]!!)
            }
            if(showDiagnosisImage.get() && files[2]!=null){
                shareArray.add(files[2]!!)
            }
            if(shareArray.isEmpty())return

//            params.imageArray=shareArray.toTypedArray()

            params.shareType = Platform.SHARE_TEXT

            JShareInterface.share(Wechat.Name, params,shareListener )
        }
    }
}