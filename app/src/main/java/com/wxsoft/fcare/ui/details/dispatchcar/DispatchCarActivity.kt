package com.wxsoft.fcare.ui.details.dispatchcar

import androidx.lifecycle.Observer
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.databinding.ActivityDispatchCarBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.dominating.DoMinaActivity
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.ui.patient.ProfileActivity

import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class DispatchCarActivity : BaseActivity() {

    private lateinit var viewModel: DispatchCarViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var carAdapter: CarAdapter
    private lateinit var doctorAdapter: UsersAdapter
    private lateinit var nurseAdapter: UsersAdapter
    private lateinit var nurseAdapter1: UsersAdapter
    private lateinit var driverAdapter: UsersAdapter
    private lateinit var driverAdapter1: UsersAdapter

    lateinit var binding: ActivityDispatchCarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = viewModelProvider(factory)
        viewModel.taskId=taskId
        binding = DataBindingUtil.setContentView<ActivityDispatchCarBinding>(this, R.layout.activity_dispatch_car)
            .apply {
                lifecycleOwner = this@DispatchCarActivity
            }
        binding.viewModel = viewModel

        carAdapter = CarAdapter(this,viewModel)
        viewModel.cars.observe(this, Observer { carAdapter.cars = it ?: emptyList() })
        binding.carList.adapter = carAdapter

        setSupportActionBar(toolbar)
        title="发车"

//        doctorAdapter = UsersAdapter(this,viewModel)
//        doctorAdapter.type = "doctor"
//        viewModel.doctors.observe(this, Observer { doctorAdapter.users = it ?: emptyList() })
//        binding.doctorList.adapter = doctorAdapter

        nurseAdapter = UsersAdapter(this,viewModel)
        nurseAdapter.type = "nurse"
        viewModel.nurses.observe(this, Observer { nurseAdapter.users = it.filter { it.hospitalId.equals("1") } })
        binding.nurseList.adapter = nurseAdapter

        nurseAdapter1 = UsersAdapter(this,viewModel)
        nurseAdapter1.type = "nurse"
        viewModel.nurses.observe(this, Observer { nurseAdapter1.users = it.filter { it.hospitalId.equals("2") } })
        binding.nurseList1.adapter = nurseAdapter1

        driverAdapter = UsersAdapter(this,viewModel)
        driverAdapter.type = "driver"
        viewModel.drivers.observe(this, Observer { driverAdapter.users = it.filter { it.hospitalId.equals("1") }})
        binding.driverList.adapter = driverAdapter

        driverAdapter1 = UsersAdapter(this,viewModel)
        driverAdapter1.type = "driver"
        viewModel.drivers.observe(this, Observer { driverAdapter1.users = it.filter { it.hospitalId.equals("2") }})
        binding.driverList1.adapter = driverAdapter1

        viewModel.task.observe(this, Observer {  })

        viewModel.taskSavedId.observe(this, Observer {
            if (it != null){
                Intent().let { intent ->
                    if (taskId.isNullOrEmpty()){toDetail(it)}
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


    private val taskId: String by lazyFast {
        intent ?.getStringExtra(ProfileActivity.TASK_ID)?:""

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_startcar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.startar->{
                viewModel.click()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}
