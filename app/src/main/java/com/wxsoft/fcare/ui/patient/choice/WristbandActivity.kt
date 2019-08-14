package com.wxsoft.fcare.ui.patient.choice

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Tag
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityWristbandBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.selecter.SelecterOfWristbandAdapter
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class WristbandActivity : BaseActivity()  {

    private lateinit var patientId:String
    private lateinit var haveSelectedId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val ID = "ID"
    }

    private lateinit var viewModel: ChoiceWrisbandViewModel

    private lateinit var tagAdapter: SelecterOfWristbandAdapter

    private val toast:Toast by  lazy {
        Toast.makeText(this,"",Toast.LENGTH_SHORT)
    }

    @Inject
    lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        haveSelectedId=intent.getStringExtra(ID)?:""
        DataBindingUtil.setContentView<ActivityWristbandBinding>(this, R.layout.activity_wristband)
            .apply {
                tagAdapter = SelecterOfWristbandAdapter(this@WristbandActivity,this@WristbandActivity.viewModel)
                firstList.adapter = tagAdapter
                viewModel = this@WristbandActivity.viewModel
                lifecycleOwner = this@WristbandActivity
            }
        viewModel.haveSelectedId = haveSelectedId
        viewModel.selectedId.set(haveSelectedId)

        viewModel.loadTags()

        viewModel.mesAction.observe(this, EventObserver {
            toast.setText(it)
            toast.show()
        })

        setSupportActionBar(toolbar)
        title="选择腕带"

        viewModel.wrisbands.observe(this, Observer {
            tagAdapter.submitList(it?: emptyList())
        })

        viewModel.unbindResult.observe(this, Observer {
            when(it){
                "success" -> checkedResult()
                "delete" -> {
                    val dialog = AlertDialog.Builder(this, R.style.Theme_FCare_Dialog)
                    dialog.setTitle("确定解除腕带绑定吗？")
                        .setPositiveButton("确定") { _, _ ->
                            viewModel.deleteWrisband(haveSelectedId)
                        }
                        .setNegativeButton("取消") { _, _ -> }
                        .create().show()
                }
                "deleteSuccess" ->{
                    Intent().let { intent->
                        val bundle = Bundle()
                        bundle.putSerializable("SelectTag", Tag())
                        intent.putExtras(bundle)
                        setResult(DaggerAppCompatActivity.RESULT_OK, intent)
                        finish()
                    }
                }
            }

        })

    }

    private fun commit(){
        if (haveSelectedId.isNullOrEmpty()){
            checkedResult()
        }else{
            val dialog = AlertDialog.Builder(this, R.style.Theme_FCare_Dialog)
            dialog.setTitle("确定切换腕带信息吗？")
                .setPositiveButton("确定") { _, _ ->
                    viewModel.unbandWrisband(haveSelectedId)
                }
                .setNegativeButton("取消") { _, _ -> }
                .create().show()
        }
    }
    private fun checkedResult(){
        Intent().let { intent->
            val bundle = Bundle()
            viewModel.wrisbands.value?.filter { it.checked }?.map {
                bundle.putSerializable("SelectTag",it)
            }
            intent.putExtras(bundle)
            setResult(DaggerAppCompatActivity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lock,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{
                commit()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}
