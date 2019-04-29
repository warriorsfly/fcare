package com.wxsoft.fcare.ui.details.vitalsigns

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityVitalSignsBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.assistant.troponin.JGDBActivity
import com.wxsoft.fcare.ui.hardwaredata.HardwareDataActivity
import com.wxsoft.fcare.ui.selecter.SelecterOfOneModelActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject
import kotlin.NumberFormatException as NumberFormatException1


class VitalSignsActivity : BaseActivity() {

    private lateinit var patientId:String
    private lateinit var id:String
    private lateinit var typeId:String
    private lateinit var xt:String

    private var contentTemp:String = ""

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val IS_XT = "IS_XT"
        const val ID = "ID"
        const val TYPE_ID = "URL"
        const val SELECT_CONCIOUS = 100
    }

    private lateinit var viewModel: VitalSignsViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityVitalSignsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityVitalSignsBinding>(this, R.layout.activity_vital_signs)
            .apply {
                line0.setOnClickListener {
                    toGetHardwareData()
                }
                lifecycleOwner = this@VitalSignsActivity
            }

        setSupportActionBar(toolbar)
        title="生命体征信息"
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        id=intent.getStringExtra(ID)?:""
        typeId=intent.getStringExtra(TYPE_ID)?:""
        xt=intent.getStringExtra(IS_XT)?:""

        viewModel.xtShow.set(xt.equals("xt"))

        binding.viewModel = viewModel

        if(id.isNotEmpty()){
            viewModel.id = id
        }else {
            viewModel.patientId = patientId
        }
        viewModel.sceneTypeId = typeId
        viewModel.loadVitalSign()

        viewModel.backToLast.observe(this, Observer {
            Intent().let { intent->
                setResult(RESULT_OK, intent)
                finish()
            }
        })

        viewModel.clickConcious.observe(this, Observer {
            when(it){
                "Conscious" ->{
                    toSelectConscious()
                }
            }
        })

        viewModel.mesAction.observe(this, EventObserver{
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
        })

        binding.breath.addTextChangedListener(object : TextWatcher {
            private val max: Int = 40
            private val min: Int = 0
            override fun afterTextChanged(s: Editable) {//输入后的监听
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {//输入前的监听
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {//输入文字产生变化的监听
                if (start >= 0) {//从一输入就开始判断，
                    if (min != -1 && max != -1) {
                        try {
                            var num = Integer.parseInt(s.toString());
                            //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                            if (num > max) {
                                binding.breath.setText(max.toString()) //如果大于max，则内容为max
                                binding.breath.setSelection(max.toString().length)
                                viewModel.vital.value?.respiration_Rate = max
                            } else if (num < min) {
                                binding.breath.setText(min.toString()) //如果小于min,则内容为min
                                binding.breath.setSelection(min.toString().length)
                                viewModel.vital.value?.respiration_Rate = min
                            }
                        } catch ( e :NumberFormatException) {
                        }
                        //edittext中的数字在max和min之间，则不做处理，正常显示即可。
                        return
                    }
                }
            }
        })
        binding.breath.setOnFocusChangeListener{ view, b ->
            if (!b){
                val max: Int = 40
                val min: Int = 25
                if (min != -1 && max != -1) {
                    try {
                        var num = Integer.parseInt(binding.breath.text.toString());
                        //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                        if (num > max) {
                            binding.breath.setText(max.toString()) //如果大于max，则内容为max
                            binding.breath.setSelection(max.toString().length)
                            viewModel.vital.value?.respiration_Rate = max
                        } else if (num < min) {
                            binding.breath.setText(min.toString()) //如果小于min,则内容为min
                            binding.breath.setSelection(min.toString().length)
                            viewModel.vital.value?.respiration_Rate = min
                        }
                    } catch ( e :NumberFormatException) {
                    }
                }
            }
        }
        binding.mb.addTextChangedListener(object : TextWatcher {
            private val max: Int = 100
            private val min: Int = 0
            override fun afterTextChanged(s: Editable) {//输入后的监听
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {//输入前的监听
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {//输入文字产生变化的监听
                if (start >= 0) {//从一输入就开始判断，
                    if (min != -1 && max != -1) {
                        try {
                            var num = Integer.parseInt(s.toString());
                            //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                            if (num > max) {
                                binding.mb.setText(max.toString()) //如果大于max，则内容为max
                                binding.mb.setSelection(max.toString().length)
                                viewModel.vital.value?.pulse_Rate = max
                            } else if (num < min) {
                                binding.mb.setText(min.toString()) //如果小于min,则内容为min
                                binding.mb.setSelection(min.toString().length)
                                viewModel.vital.value?.pulse_Rate = min
                            }
                        } catch ( e :NumberFormatException) {
                        }
                        //edittext中的数字在max和min之间，则不做处理，正常显示即可。
                        return
                    }
                }
            }
        })
        binding.mb.setOnFocusChangeListener{ view, b ->
            if (!b){
                val max: Int = 100
                val min: Int = 30
                if (min != -1 && max != -1) {
                    try {
                        var num = Integer.parseInt(binding.mb.text.toString());
                        //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                        if (num > max) {
                            binding.mb.setText(max.toString()) //如果大于max，则内容为max
                            binding.mb.setSelection(max.toString().length)
                            viewModel.vital.value?.pulse_Rate = max
                        } else if (num < min) {
                            binding.mb.setText(min.toString()) //如果小于min,则内容为min
                            binding.mb.setSelection(min.toString().length)
                            viewModel.vital.value?.pulse_Rate = min
                        }
                    } catch ( e :NumberFormatException) {
                    }
                }
            }
        }

        binding.heart.addTextChangedListener(object : TextWatcher {
            private val max: Int = 100
            private val min: Int = 0
            override fun afterTextChanged(s: Editable) {//输入后的监听
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {//输入前的监听
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {//输入文字产生变化的监听
                if (start >= 0) {//从一输入就开始判断，
                    if (min != -1 && max != -1) {
                        try {
                            var num = Integer.parseInt(s.toString());
                            //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                            if (num > max) {
                                binding.heart.setText(max.toString()) //如果大于max，则内容为max
                                binding.heart.setSelection(max.toString().length)
                                viewModel.vital.value?.heart_Rate = max
                            } else if (num < min) {
                                binding.heart.setText(min.toString()) //如果小于min,则内容为min
                                binding.heart.setSelection(min.toString().length)
                                viewModel.vital.value?.heart_Rate = min
                            }
                        } catch ( e :NumberFormatException) {
                        }
                        //edittext中的数字在max和min之间，则不做处理，正常显示即可。
                        return
                    }
                }
            }
        })
        binding.heart.setOnFocusChangeListener{ view, b ->
            if (!b){
                val max: Int = 100
                val min: Int = 30
                if (min != -1 && max != -1) {
                    try {
                        var num = Integer.parseInt(binding.heart.text.toString());
                        //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                        if (num > max) {
                            binding.heart.setText(max.toString()) //如果大于max，则内容为max
                            binding.heart.setSelection(max.toString().length)
                            viewModel.vital.value?.heart_Rate = max
                        } else if (num < min) {
                            binding.heart.setText(min.toString()) //如果小于min,则内容为min
                            binding.heart.setSelection(min.toString().length)
                            viewModel.vital.value?.heart_Rate = min
                        }
                    } catch ( e :NumberFormatException) {
                    }
                }
            }
        }
        binding.bloodOxygen.addTextChangedListener(object : TextWatcher {
            private val max: Int = 100
            private val min: Int = 0
            override fun afterTextChanged(s: Editable) {//输入后的监听
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {//输入前的监听
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {//输入文字产生变化的监听
                if (start >= 0) {//从一输入就开始判断，
                    if (min != -1 && max != -1) {
                        try {
                            var num = Integer.parseInt(s.toString());
                            //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                            if (num > max) {
                                binding.bloodOxygen.setText(max.toString()) //如果大于max，则内容为max
                                binding.bloodOxygen.setSelection(max.toString().length)
                                viewModel.vital.value?.spO2 = max
                            } else if (num < min) {
                                binding.bloodOxygen.setText(min.toString()) //如果小于min,则内容为min
                                binding.bloodOxygen.setSelection(min.toString().length)
                                viewModel.vital.value?.spO2 = min
                            }
                        } catch ( e :NumberFormatException) {
                        }
                        //edittext中的数字在max和min之间，则不做处理，正常显示即可。
                        return
                    }
                }
            }
        })
        binding.bloodOxygen.setOnFocusChangeListener{ view, b ->
            if (!b){
                val max: Int = 100
                val min: Int = 70
                if (min != -1 && max != -1) {
                    try {
                        var num = Integer.parseInt(binding.bloodOxygen.text.toString());
                        //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                        if (num > max) {
                            binding.bloodOxygen.setText(max.toString()) //如果大于max，则内容为max
                            binding.bloodOxygen.setSelection(max.toString().length)
                            viewModel.vital.value?.spO2 = max
                        } else if (num < min) {
                            binding.bloodOxygen.setText(min.toString()) //如果小于min,则内容为min
                            binding.bloodOxygen.setSelection(min.toString().length)
                            viewModel.vital.value?.spO2 = min
                        }
                    } catch ( e :NumberFormatException) {
                    }
                }
            }
        }
        binding.temperature.addTextChangedListener(object : TextWatcher {
            private val max: Float = 45.0f
            private val min: Float = 0.0f
            override fun afterTextChanged(s: Editable) {//输入后的监听
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {//输入前的监听
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {//输入文字产生变化的监听
                if (start >= 0) {//从一输入就开始判断，
                    if (min != -1.0f && max != -1.0f) {
                        try {
                            var num = s.toString().toFloat()
                            //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                            if (num > max) {
                                binding.temperature.setText(max.toString()) //如果大于max，则内容为max
                                binding.temperature.setSelection(max.toString().length)
                                viewModel.vital.value?.body_Temperature = max
                            } else if (num < min) {
                                binding.temperature.setText(min.toString()) //如果小于min,则内容为min
                                binding.temperature.setSelection(min.toString().length)
                                viewModel.vital.value?.body_Temperature = min
                            }
                        } catch ( e :NumberFormatException) {
                        }
                        //edittext中的数字在max和min之间，则不做处理，正常显示即可。
                        return
                    }
                }
            }
        })

        binding.temperature.setOnFocusChangeListener{ view, b ->
            if (!b){
                val max: Float = 45.0f
                val min: Float = 33.0f
                if (min != -1.0f && max != -1.0f) {
                    try {
                        var num = Integer.parseInt(binding.temperature.text.toString());
                        //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                        if (num > max) {
                            binding.temperature.setText(max.toString()) //如果大于max，则内容为max
                            binding.temperature.setSelection(max.toString().length)
                            viewModel.vital.value?.body_Temperature = max
                        } else if (num < min) {
                            binding.temperature.setText(min.toString()) //如果小于min,则内容为min
                            binding.temperature.setSelection(min.toString().length)
                            viewModel.vital.value?.body_Temperature = min
                        }
                    } catch ( e :NumberFormatException) {
                    }
                }
            }
        }


    }



    fun toSelectConscious(){
        val intent = Intent(this, SelecterOfOneModelActivity::class.java).apply {
            putExtra(SelecterOfOneModelActivity.PATIENT_ID, patientId)
            putExtra(SelecterOfOneModelActivity.COME_FROM, "Vital")
            putExtra(SelecterOfOneModelActivity.ID, viewModel.vital.value?.consciousness_Type)
        }
        startActivityForResult(intent,SELECT_CONCIOUS )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                SELECT_CONCIOUS ->{//
                    val conscious= data?.getSerializableExtra("SelectOne") as Dictionary
                    viewModel.vital.value?.consciousness_Type = conscious.id
                    viewModel.vital.value?.consciousnesTypeName = conscious.itemName
                }

            }
        }
    }


    fun toGetHardwareData(){
        val intent = Intent(this@VitalSignsActivity, HardwareDataActivity::class.java).apply {
            putExtra(HardwareDataActivity.PATIENT_ID, patientId)
        }
        startActivityForResult(intent, JGDBActivity.HARDWARE_DATA)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_subject,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{
                viewModel.click()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}
