package com.wxsoft.fcare.ui.details.dispatchcar

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.ActivityDispatchCarBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.dominating.DoMinaActivity
import com.wxsoft.fcare.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class DispatchCarActivity : BaseActivity() {

    private lateinit var viewModel: DispatchCarViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var carAdapter: CarAdapter
    lateinit var doctorAdapter: UsersAdapter
    lateinit var nurseAdapter: UsersAdapter
    lateinit var driverAdapter: UsersAdapter

    lateinit var binding: ActivityDispatchCarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityDispatchCarBinding>(this, R.layout.activity_dispatch_car)
            .apply {
                lifecycleOwner = this@DispatchCarActivity
            }
        binding.viewModel = viewModel

//        viewModel.mesAction.observe(this, EventObserver{ t->
//            toDetail(t)
//        })
        carAdapter = CarAdapter(this,viewModel)
        viewModel.cars.observe(this, Observer { it -> carAdapter.cars = it ?: emptyList() })
        binding.carList.adapter = carAdapter

        back.setOnClickListener { onBackPressed() }

        doctorAdapter = UsersAdapter(this,viewModel)
        doctorAdapter.type = "doctor"
        viewModel.doctors.observe(this, Observer { it -> doctorAdapter.users = it ?: emptyList() })
        binding.doctorList.adapter = doctorAdapter

        nurseAdapter = UsersAdapter(this,viewModel)
        nurseAdapter.type = "nurse"
        viewModel.nurses.observe(this, Observer { it -> nurseAdapter.users = it ?: emptyList() })
        binding.nurseList.adapter = nurseAdapter

        driverAdapter = UsersAdapter(this,viewModel)
        driverAdapter.type = "driver"
        viewModel.drivers.observe(this, Observer { it -> driverAdapter.users = it ?: emptyList() })
        binding.driverList.adapter = driverAdapter

        viewModel.task.observe(this, Observer {  })

        viewModel.taskId.observe(this, Observer {
            if (it != null){

                Intent().let { intent ->

                    toDetail(it)
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

        })
        viewModel.haveSelectCar.observe(this, Observer {
            Toast.makeText(this@DispatchCarActivity, "请先选择车辆", Toast.LENGTH_SHORT).show()

        })

    }



    private fun toDetail(id:String) {
        var intent = Intent(this, DoMinaActivity::class.java).apply {
            putExtra(DoMinaActivity.TASK_ID, id)
        }
        startActivity(intent)
    }


}
