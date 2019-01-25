package com.wxsoft.fcare.ui.main.fragment.task


import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.FragmentAssignmentBinding
import com.wxsoft.fcare.ui.details.checkbody.CheckBodyActivity
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseActivity
import com.wxsoft.fcare.ui.details.dispatchcar.DispatchCarActivity
import com.wxsoft.fcare.ui.details.dominating.DoMinaActivity
import com.wxsoft.fcare.ui.details.measures.MeasuresActivity
import com.wxsoft.fcare.ui.details.medicalhistory.MedicalHistoryActivity
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity
import com.wxsoft.fcare.utils.DateTimeUtils
import com.wxsoft.fcare.utils.activityViewModelProvider
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_assignment.*
import java.util.*
import javax.inject.Inject


class TaskFragment : DaggerFragment() {

    private lateinit var viewModel: TaskViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activityViewModelProvider(viewModelFactory)

        val binding = FragmentAssignmentBinding.inflate(inflater, container, false).apply {

            floatingActionButton.setOnClickListener {
                toDispatchCar()
            }
            selectTaskDate.setOnClickListener {
                selectTime()
            }
            viewModel = this@TaskFragment.viewModel

            this@TaskFragment.adapter= TaskAdapter(this@TaskFragment, this@TaskFragment.viewModel)
            list.adapter = this@TaskFragment.adapter

            lifecycleOwner = this@TaskFragment
        }
        viewModel.tasks.observe(this, Observer { it -> adapter.tasks = it ?: emptyList() })

        viewModel.navigateToOperationAction.observe(this, EventObserver { t ->
            toDetail(t)
        })
        return binding.root
    }

    fun selectTime() {
        var ca = Calendar.getInstance()
        var mYear = ca.get(Calendar.YEAR)
        var mMonth = ca.get(Calendar.MONTH)
        var mDay = ca.get(Calendar.DAY_OF_MONTH)
        var dialog =
            DatePickerDialog(this.context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                mYear = year
                mMonth = monthOfYear
                mDay = dayOfMonth
                viewModel.taskDate = year.toString() + "-" +
                        DateTimeUtils.frontCompWithZore(monthOfYear + 1, 2) + "-" +
                        DateTimeUtils.frontCompWithZore(dayOfMonth, 2)
                select_task_date.text = viewModel.taskDate
                viewModel.onSwipeRefresh()
            }, mYear, mMonth, mDay)

        dialog.show()
    }


    fun toDispatchCar() {
//        toDiagnose()
        startTask()
    }

    fun startTask(){
        var intent = Intent(activity!!, DispatchCarActivity::class.java)
        startActivity(intent)
    }

    fun toDetail(id: String) {
        var intent = Intent(activity!!, DoMinaActivity::class.java)
        intent.putExtra(DoMinaActivity.TASK_ID, id)
        startActivity(intent)
    }

    fun toVital(){//生命体征录入
        var intent = Intent(activity!!, VitalSignsActivity::class.java)
        intent.putExtra(VitalSignsActivity.PATIENT_ID,"d6bf2a1287a64cc1bad9691c46a31fd5")
        startActivity(intent)
    }

    fun toCheckBody(){//PhysicalExamination
        var intent = Intent(activity!!, CheckBodyActivity::class.java)
        intent.putExtra(CheckBodyActivity.PATIENT_ID,"d6bf2a1287a64cc1bad9691c46a31fd5")
        startActivity(intent)
    }

    fun toMedicalHistory(){//IllnessHistory
        var intent = Intent(activity!!, MedicalHistoryActivity::class.java)
        intent.putExtra(MedicalHistoryActivity.PATIENT_ID,"d6bf2a1287a64cc1bad9691c46a31fd5")
        startActivity(intent)
    }

    fun toMeasures(){//DispostionMeasures
        var intent = Intent(activity!!, MeasuresActivity::class.java)
        intent.putExtra(MeasuresActivity.PATIENT_ID,"d6bf2a1287a64cc1bad9691c46a31fd5")
        startActivity(intent)
    }

    fun toDiagnose(){//诊断
        var intent = Intent(activity!!, DiagnoseActivity::class.java)
        intent.putExtra(DiagnoseActivity.PATIENT_ID,"d6bf2a1287a64cc1bad9691c46a31fd5")
        startActivity(intent)
    }

}
