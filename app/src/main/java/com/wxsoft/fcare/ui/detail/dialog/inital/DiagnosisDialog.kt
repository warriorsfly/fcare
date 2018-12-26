package com.wxsoft.fcare.ui.detail.dialog.inital


import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.chip.Chip
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.databinding.DialogFirstDiagnosisBinding
import com.wxsoft.fcare.databinding.ItemDiagnosisBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.utils.viewModelProvider
import com.wxsoft.fcare.widget.WxDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class DiagnosisDialog : WxDimDialogFragment(), HasSupportFragmentInjector {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: DiagnosisViewModel
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    var patientId:String=""

    private lateinit var binding:DialogFirstDiagnosisBinding

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel=viewModelProvider(factory)

        binding.viewModel=viewModel

        viewModel.liveData.observe(this, Observer {
            dismiss()
        })
        viewModel.diagnosis4Type.observe(this, Observer {
            if(it!=null && it.isNotEmpty()){
                binding.disgnosis4.removeAllViews()
                for (dic in it) {
                    val itemBinding = ItemDiagnosisBinding.inflate(layoutInflater, binding.disgnosis4, true)
                    itemBinding.root.id=it.indexOf(dic)
                    itemBinding.diagnosis=dic

                }
            }
        })

        viewModel.diagnosis27Type.observe(this, Observer {
            if(it!=null && it.isNotEmpty()){
                binding.disgnosis27.removeAllViews()
                for (dic in it) {
                    val itemBinding = ItemDiagnosisBinding.inflate(layoutInflater, binding.disgnosis27, true)
                    itemBinding.diagnosis=dic
                    itemBinding.root.id=it.indexOf(dic)
                    itemBinding.filterLabel.setOnClickListener {viewModel.select27()}
                }
            }
        })

        viewModel.liveDiagnosis.observe(this, Observer {
            if(it!=null){
                binding.disgnosis4.setOnCheckedChangeListener { chipGroup, i ->
                    if(i>=0 ) {
                        val chip: Chip = chipGroup.findViewById(i)
                        val itemBinding = DataBindingUtil.findBinding<ItemDiagnosisBinding>(chip)!!
                        viewModel.select4(itemBinding.diagnosis!!.itemCode, i)
                    }else{
                        viewModel.select4("",i)
                    }
                }
            }
        })




        viewModel.patientId=patientId
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=DialogFirstDiagnosisBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@DiagnosisDialog)
        }

        return binding.root
    }

    companion object {
        const val TAG = "diagnosis_dialog"
    }
}
