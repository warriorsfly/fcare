package com.wxsoft.fcare.ui.main.fragment.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.FragmentUserProfileBinding
import com.wxsoft.fcare.ui.login.LoginActivity
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class UserProfileFragment : DaggerFragment() {


    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: UserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activityViewModelProvider(factory)
        val binding = FragmentUserProfileBinding.inflate(inflater, container, false).apply {

            viewModel=this@UserProfileFragment.viewModel
            this@UserProfileFragment.viewModel.getAllHospital()
            logout.setOnClickListener {
                viewModel?.loginOut()
                val intent = Intent(activity!!, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(intent)
                activity?.finish()
            }
            changePass.setOnClickListener {
                val dialog=PasswordFragment()
                dialog.show(childFragmentManager,"all")
            }
            changeHospital.setOnClickListener {
                if (this@UserProfileFragment.viewModel.hospitals.value!=null) showDiag()
            }

            lifecycleOwner = this@UserProfileFragment
        }

        viewModel.hospitals.observe(this@UserProfileFragment, Observer {})
        viewModel.mesAction.observe(this, EventObserver{
            Toast.makeText(this.context,it, Toast.LENGTH_LONG).show()
        })


        return binding.root
    }

    fun showDiag(){
        val list = viewModel.hospitals.value!!.map { it.name }.toTypedArray()
        AlertDialog.Builder(this.context!!).setItems(list) { _, i ->
            val hospital = viewModel.hospitals.value!![i]
            viewModel.selectHospital(hospital)
        }.create().show()
    }

}
