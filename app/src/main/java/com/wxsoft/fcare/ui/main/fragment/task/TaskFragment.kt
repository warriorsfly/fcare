package com.wxsoft.fcare.ui.main.fragment.task


import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.widget.PopupWindowCompat
import androidx.lifecycle.Observer
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentAssignmentBinding
import com.wxsoft.fcare.databinding.LayoutTaskSelectDateBinding
import com.wxsoft.fcare.databinding.LayoutTaskSelectTypeBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.BaseTimingFragment
import com.wxsoft.fcare.ui.details.dispatchcar.DispatchCarActivity
import com.wxsoft.fcare.ui.details.dominating.DoMinaActivity
import com.wxsoft.fcare.ui.main.fragment.task.searchtask.SearchTaskActivity
import java.util.*
import javax.inject.Inject


class TaskFragment : BaseTimingFragment() {
    override fun selectTime(millseconds: Long) {

        when(selectedId){
            100 -> viewModel.checkCondition.value?.startDate = DateTimeUtils.formatter.format(millseconds)
            200 -> viewModel.checkCondition.value?.endDate = DateTimeUtils.formatter.format(millseconds)
        }
    }

    private var selectedId=0

    companion object {
        const val NEW_TAK_REQUEST_CODE=14
    }

    private lateinit var viewModel: TaskViewModel

    private val popwindow:PopupWindow by lazy {
        PopupWindow().apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            width = ViewGroup.LayoutParams.MATCH_PARENT
            this.isOutsideTouchable = true
            isFocusable = true
            this.setBackgroundDrawable(ColorDrawable(Color.GRAY))
        }
    }
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var binding: FragmentAssignmentBinding

    lateinit var adapter: TaskAdapter

    lateinit var selectDatebinding: LayoutTaskSelectDateBinding
    lateinit var selectTypebinding: LayoutTaskSelectTypeBinding
    lateinit var selectTypeAdapter: TaskSelectTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activityViewModelProvider(viewModelFactory)
        adapter= TaskAdapter(this, viewModel)
        viewModel.showPatients("")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


         binding = FragmentAssignmentBinding.inflate(inflater, container, false).apply {

            floatingActionButton.setOnClickListener {
                toDispatchCar()
            }
//            selectTaskDate.setOnClickListener {
//                selectTime()
//            }
            viewModel = this@TaskFragment.viewModel

//            this@TaskFragment.adapter= TaskAdapter(this@TaskFragment, this@TaskFragment.viewModel)
             taskList.adapter = this@TaskFragment.adapter

            lifecycleOwner = this@TaskFragment
        }

        selectDatebinding = LayoutTaskSelectDateBinding.inflate(inflater, container, false).apply {
            viewModel=this@TaskFragment.viewModel
            lifecycleOwner = this@TaskFragment
        }
        selectTypebinding = LayoutTaskSelectTypeBinding.inflate(inflater, container, false).apply {
            viewModel=this@TaskFragment.viewModel
            lifecycleOwner = this@TaskFragment
        }

        viewModel.tasks.observe(this, Observer {
            viewModel.noPatientsShow.set(it.snapshot().size==0)
            adapter.submitList(it)
        })

        viewModel.navigateToOperationAction.observe(this, EventObserver {
            toDetail(it)
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
                    dialog?.show(childFragmentManager, "all")

                }
                "结束时间" ->{
                    selectedId = 200
                    val currentTime = viewModel.checkCondition.value?.endDate.let { text ->
                        if (text!!.isEmpty()) 0L else DateTimeUtils.formatter.parse(text).time
                    }
                    dialog = createDialog(currentTime)
                    dialog?.show(childFragmentManager, "all")
                }
                "选择时间确定" ->{
                    popwindow.dismiss()
                }
                "选择类型确定" ->{
                    popwindow.dismiss()
                }
            }
        })
        selectTypeAdapter = TaskSelectTypeAdapter(this@TaskFragment,viewModel)
        selectTypebinding.typeList.adapter = selectTypeAdapter
        viewModel.typeItems.observe(this, Observer {
            selectTypeAdapter.items = it
        })


        return binding.root
    }

    private fun selectTime() {
        val ca = Calendar.getInstance()
        var mYear = ca.get(Calendar.YEAR)
        var mMonth = ca.get(Calendar.MONTH)
        var mDay = ca.get(Calendar.DAY_OF_MONTH)
        val dialog =
            DatePickerDialog(this.context!!, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                mYear = year
                mMonth = monthOfYear
                mDay = dayOfMonth
                viewModel.taskDate = year.toString() + "-" +
                        DateTimeUtils.frontCompWithZore(monthOfYear + 1, 2) + "-" +
                        DateTimeUtils.frontCompWithZore(dayOfMonth, 2)
//                select_task_date.text = viewModel.taskDate
                viewModel.onSwipeRefresh()
            }, mYear, mMonth, mDay)

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==RESULT_OK){
            when(requestCode){
                NEW_TAK_REQUEST_CODE ->viewModel.onSwipeRefresh()
            }
        }
    }


    private fun toDispatchCar() {

        startTask()
    }

    private fun startTask(){
        val intent = Intent(activity!!, DispatchCarActivity::class.java)
        startActivityForResult(intent, NEW_TAK_REQUEST_CODE)
    }

    private fun toDetail(id: String) {
        val intent = Intent(activity!!, DoMinaActivity::class.java)
        intent.putExtra(DoMinaActivity.TASK_ID, id)
        startActivityForResult(intent,NEW_TAK_REQUEST_CODE)
    }


    private fun selectDate(){
        viewModel.clickTopResult.value="DONE"
        popwindow.contentView = selectDatebinding.root
        PopupWindowCompat.showAsDropDown(popwindow, binding.patientsTopBar, 0, 0, Gravity.START)
    }
    private fun selectType(){
        viewModel.clickTopResult.value="DONE"
        popwindow.contentView = selectTypebinding.root
        PopupWindowCompat.showAsDropDown(popwindow, binding.patientsTopBar, 0, 0, Gravity.START)
    }


    fun toSearchPatient(){
        viewModel.clickTopResult.value="DONE"
        Intent(activity!!, SearchTaskActivity::class.java).let {
            startActivityForResult(it, BaseActivity.NEW_PATIENT_REQUEST)
        }
    }



    override fun onPause() {
        if (popwindow.isShowing)
            popwindow.dismiss()
        super.onPause()

    }

}
