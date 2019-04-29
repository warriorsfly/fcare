package com.wxsoft.fcare.ui.details.checkbody.select


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivitySelectBodyItemsBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class SelectBodyItemsActivity : BaseActivity() {

    private lateinit var patientId:String
    private lateinit var selectId:String
    private lateinit var selected1:String
    private lateinit var selected2:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val SELECT_TYPE_ID = "SELECT_TYPE_ID"
        const val SELECT_ID_1 = "SELECT_ID_1"
        const val SELECT_ID_2 = "SELECT_ID_2"
    }

    private lateinit var viewModel: SelectBodyItemsViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivitySelectBodyItemsBinding
    lateinit var adapter1: SelectBodyItemsAdapter
    lateinit var adapter2: SelectBodyItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivitySelectBodyItemsBinding>(this, R.layout.activity_select_body_items)
            .apply {
                adapter1 = SelectBodyItemsAdapter(this@SelectBodyItemsActivity,this@SelectBodyItemsActivity.viewModel)
                adapter2 = SelectBodyItemsAdapter(this@SelectBodyItemsActivity,this@SelectBodyItemsActivity.viewModel)
                firstList.adapter = adapter1
                secondList.adapter = adapter2

                viewModel = this@SelectBodyItemsActivity.viewModel
                lifecycleOwner = this@SelectBodyItemsActivity
            }
        patientId=intent.getStringExtra(SelectBodyItemsActivity.PATIENT_ID)?:""
        selectId=intent.getStringExtra(SelectBodyItemsActivity.SELECT_TYPE_ID)?:""
        selected1=intent.getStringExtra(SelectBodyItemsActivity.SELECT_ID_1)?:""
        selected2=intent.getStringExtra(SelectBodyItemsActivity.SELECT_ID_2)?:""

        setSupportActionBar(toolbar)
        when(selectId){
            "1"-> title="选择检查过程"
            "2"-> title="选择皮肤症状"
            "3"-> title="选择左瞳孔症状"
            "4"-> title="选择右瞳孔症状"
        }

        viewModel.patientId = patientId
        viewModel.typeId = selectId
        viewModel.selectedId1 = selected1
        viewModel.selectedId2 = selected2

        viewModel.coordinationItems.observe(this, Observer {
            adapter1.submitList(it)
        })
        viewModel.skinItems.observe(this, Observer {
            adapter1.submitList(it)
        })
        viewModel.leftPupilsItems.observe(this, Observer {
            adapter1.submitList(it)
        })
        viewModel.leftResponseLightItems.observe(this, Observer {
            adapter2.submitList(it)
        })
        viewModel.rightPupilsItems.observe(this, Observer {
            adapter1.submitList(it)
        })
        viewModel.rightResponseLightItems.observe(this, Observer {
            adapter2.submitList(it)
        })


        viewModel.submit.observe(this, Observer {
            if (selectId.equals("1")||selectId.equals("2")){
                complit()
            }
        })

    }


    fun complit(){//确定
        Intent().let { intent->
            val bundle = Bundle()
            when(selectId){
                "1"->{
                    viewModel.coordinationItems.value?.filter { it.checked }
                        ?.map {
                            bundle.putSerializable("coordination",it.id)
                            bundle.putSerializable("coordinationName",it.itemName)
                        }
                }
                "2"->{
                    viewModel.skinItems.value?.filter { it.checked }
                        ?.map {
                            bundle.putSerializable("skin",it.id)
                            bundle.putSerializable("skinName",it.itemName)
                        }
                }
                "3"->{
                    viewModel.leftPupilsItems.value?.filter { it.checked }
                        ?.map {
                            bundle.putSerializable("leftPupils",it.id)
                            bundle.putSerializable("leftPupilsName",it.itemName)
                        }
                    viewModel.leftResponseLightItems.value?.filter { it.checked }
                        ?.map {
                            bundle.putSerializable("leftResponse",it.id)
                            bundle.putSerializable("leftResponseName",it.itemName)
                        }
                }
                "4"->{
                    viewModel.rightPupilsItems.value?.filter { it.checked }
                        ?.map {
                            bundle.putSerializable("rightPupils",it.id)
                            bundle.putSerializable("rightPupilsName",it.itemName)
                        }
                    viewModel.rightResponseLightItems.value?.filter { it.checked }
                        ?.map {
                            bundle.putSerializable("rightResponse",it.id)
                            bundle.putSerializable("rightResponseName",it.itemName)
                        }
                }

            }
            intent.putExtras(bundle)
            setResult(RESULT_OK, intent)
            finish()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (selectId.equals("3")||selectId.equals("4")) menuInflater.inflate(R.menu.menu_sure,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.sure->{
                complit()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}
