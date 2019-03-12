package com.wxsoft.fcare.ui.details.dispatchcar

import androidx.lifecycle.Observer
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityDispatchCarBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.dominating.DoMinaActivity
import com.wxsoft.fcare.core.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class DispatchCarActivity : BaseActivity() {

    private lateinit var viewModel: DispatchCarViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var carAdapter: CarAdapter
    private lateinit var doctorAdapter: UsersAdapter
    private lateinit var nurseAdapter: UsersAdapter
    private lateinit var driverAdapter: UsersAdapter

    lateinit var binding: ActivityDispatchCarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityDispatchCarBinding>(this, R.layout.activity_dispatch_car)
            .apply {
                lifecycleOwner = this@DispatchCarActivity
            }
        binding.viewModel = viewModel

        carAdapter = CarAdapter(this,viewModel)
        viewModel.cars.observe(this, Observer { carAdapter.cars = it ?: emptyList() })
        binding.carList.adapter = carAdapter

        back.setOnClickListener { onBackPressed() }

        doctorAdapter = UsersAdapter(this,viewModel)
        doctorAdapter.type = "doctor"
        viewModel.doctors.observe(this, Observer { doctorAdapter.users = it ?: emptyList() })
        binding.doctorList.adapter = doctorAdapter

        nurseAdapter = UsersAdapter(this,viewModel)
        nurseAdapter.type = "nurse"
        viewModel.nurses.observe(this, Observer { nurseAdapter.users = it ?: emptyList() })
        binding.nurseList.adapter = nurseAdapter

        driverAdapter = UsersAdapter(this,viewModel)
        driverAdapter.type = "driver"
        viewModel.drivers.observe(this, Observer { driverAdapter.users = it ?: emptyList() })
        binding.driverList.adapter = driverAdapter

        viewModel.task.observe(this, Observer {  })

        viewModel.taskId.observe(this, Observer {
            if (it != null){

                Intent().let { intent ->
                    toDetail(it)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        })
        viewModel.haveSelectCar.observe(this, Observer {
            Toast.makeText(this@DispatchCarActivity, "请先选择车辆", Toast.LENGTH_SHORT).show()

        })

    }

    private fun toDetail(id:String) {
        val intent = Intent(this, DoMinaActivity::class.java).apply {
            putExtra(DoMinaActivity.TASK_ID, id)
        }
        startActivity(intent)
    }


}
