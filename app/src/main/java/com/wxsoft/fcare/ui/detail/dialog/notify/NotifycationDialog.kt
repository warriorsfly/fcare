package com.wxsoft.fcare.ui.detail.dialog.notify


import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.BindingAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.data.entity.Department
import com.wxsoft.fcare.databinding.DialogNotifycationBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.result.Resource
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
class NotifycationDialog : WxDimDialogFragment(), HasSupportFragmentInjector {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: NotifyViewModel

    private lateinit var binding:DialogNotifycationBinding

    var patientId:String=""

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

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
        viewModel.patientId=patientId

        viewModel.notifyResult.observe(this, Observer{
            if(it is Resource.Success){
                dismiss()
            }
        })

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DialogNotifycationBinding.inflate(inflater,container,false).apply {
            setLifecycleOwner(this@NotifycationDialog)
        }

        return binding.root
    }

    companion object {
        const val TAG = "notify_dialog"
    }

}

@BindingAdapter(value = ["departmentItems"])
fun departmentItems(recyclerView: RecyclerView, list: List<Department>?) {
    if (recyclerView.adapter == null) {
        recyclerView.adapter = DepartmentAdapter()

    }

    (recyclerView.adapter as DepartmentAdapter).apply { submitList(list?: emptyList()) }
}