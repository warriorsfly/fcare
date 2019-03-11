package com.wxsoft.fcare.ui.main.fragment.patients

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import androidx.appcompat.widget.SearchView
import androidx.core.widget.PopupWindowCompat
import androidx.lifecycle.Observer
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentPatientsBinding
import com.wxsoft.fcare.databinding.LayoutPatientSelectDateBinding
import com.wxsoft.fcare.databinding.LayoutPatientSelectTypeBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.main.fragment.patients.searchpatients.SearchPatientsActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.workspace.WorkingActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class PatientsFragment : DaggerFragment() , OnDateSetListener{

    private var dialog: TimePickerDialog?=null


    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {

        when(selectedId){
            100 -> viewModel.checkCondition.value?.startDate = DateTimeUtils.formatter.format(millseconds)
            200 -> viewModel.checkCondition.value?.endDate = DateTimeUtils.formatter.format(millseconds)
        }
    }

    private var selectedId=0



    private lateinit var viewModel: PatientsViewModel

    private lateinit var popwindow:PopupWindow

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var adapter: PatientsAdapter
    lateinit var binding: FragmentPatientsBinding
    lateinit var selectDatebinding: LayoutPatientSelectDateBinding
    lateinit var selectTypebinding: LayoutPatientSelectTypeBinding
    lateinit var selectTypeAdapter: PatientSelectTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activityViewModelProvider(viewModelFactory)

        adapter= PatientsAdapter(this,viewModel)
        viewModel.showPatients("")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentPatientsBinding.inflate(inflater, container, false).apply {
            list.adapter=adapter
            viewModel=this@PatientsFragment.viewModel

//            search.setOnQueryTextListener(this@PatientsFragment)
            floatingActionButton.setOnClickListener {
                val intent = Intent(activity!!, ProfileActivity::class.java)
                startActivityForResult(intent, BaseActivity.NEW_PATIENT_REQUEST)
            }
            lifecycleOwner = this@PatientsFragment

        }

        selectDatebinding = LayoutPatientSelectDateBinding.inflate(inflater, container, false).apply {
            viewModel=this@PatientsFragment.viewModel
            lifecycleOwner = this@PatientsFragment
        }
        selectTypebinding = LayoutPatientSelectTypeBinding.inflate(inflater, container, false).apply {
            viewModel=this@PatientsFragment.viewModel
            lifecycleOwner = this@PatientsFragment
        }

        popwindow = PopupWindow().apply {
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT-60)
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
            setOutsideTouchable(true)
            setFocusable(true)
            setBackgroundDrawable(ColorDrawable(Color.GRAY))
        }

        viewModel.patients.observe(this, Observer {
            adapter.submitList(it)
        })
        viewModel.detailAction.observe(this, EventObserver{
                t->
            toDetail(t)
        })

        viewModel.clickTop.observe(this, Observer {
            when(it){
                "DATE" -> selectDate()
                "TYPE" -> selectType()
                "SEARCH" -> toSearchPatient()
            }
        })

        viewModel.clickCusDate.observe(this, Observer {
            when(it){
                "开始时间" ->{
                    selectedId = 100
                    val currentTime = viewModel.checkCondition.value?.startDate.let { text ->
                        if (text!!.isEmpty()) 0L else DateTimeUtils.formatter.parse(text).time
                    }
                    dialog = createDialog(currentTime)
                    dialog?.show(fragmentManager, "all")

                }
                "结束时间" ->{
                    selectedId = 200
                    val currentTime = viewModel.checkCondition.value?.endDate.let { text ->
                        if (text!!.isEmpty()) 0L else DateTimeUtils.formatter.parse(text).time
                    }
                    dialog = createDialog(currentTime)
                    dialog?.show(fragmentManager, "all")
                }
                "选择时间确定" ->{
                    popwindow.dismiss()
                }
                "选择类型确定" ->{
                    popwindow.dismiss()
                }
            }
        })
        selectTypeAdapter = PatientSelectTypeAdapter(this@PatientsFragment,viewModel)
        selectTypebinding.typeList.adapter = selectTypeAdapter
        viewModel.typeItems.observe(this, Observer {
            selectTypeAdapter.items = it
        })

        return binding.root
    }

    private fun toDetail(id:String) {

        Intent(activity!!, WorkingActivity::class.java).let {
            it.putExtra(ProfileActivity.PATIENT_ID,id)
            startActivityForResult(it, BaseActivity.NEW_PATIENT_REQUEST)
        }

    }

//    override fun onStop() {
//        search.clearFocus()
//        super.onStop()
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                BaseActivity.NEW_PATIENT_REQUEST -> {
//                    viewModel.showPatients(search.query.toString())
                }
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//        search.clearFocus()
//    }


    private fun selectDate(){
        popwindow.setContentView(selectDatebinding.root)
        PopupWindowCompat.showAsDropDown(popwindow, binding.searchPlaceholder, 0, 0, Gravity.START)
    }
    private fun selectType(){
        popwindow.setContentView(selectTypebinding.root)
        PopupWindowCompat.showAsDropDown(popwindow, binding.searchPlaceholder, 0, 0, Gravity.START)
    }


    fun toSearchPatient(){
        Intent(activity!!, SearchPatientsActivity::class.java).let {
            startActivityForResult(it, BaseActivity.NEW_PATIENT_REQUEST)
        }
    }

    private fun createDialog(time:Long): TimePickerDialog {

        return TimePickerDialog.Builder()
            .setCallBack(this)
            .setCancelStringId("取消")
            .setSureStringId("确定")
            .setTitleStringId("选择时间")
            .setYearText("")
            .setMonthText("")
            .setDayText("")
            .setHourText("")
            .setMinuteText("")
            .setCyclic(false)
            .setCurrentMillseconds(if(time==0L)System.currentTimeMillis() else time)
            .setType(Type.ALL)
            .setWheelItemTextSize(16)
            .build()
    }

}
