package com.wxsoft.fcare.ui.main.fragment.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.wxsoft.fcare.core.di.ViewModelFactory
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
                this@UserProfileFragment.viewModel.getAllHospital()
            }

            lifecycleOwner = this@UserProfileFragment
        }

        viewModel.hospitals.observe(this@UserProfileFragment, Observer {
            if (it!!.size>0){
                val list = it.map { it.name }.toTypedArray()
                AlertDialog.Builder(this.context!!).setItems(list) { _, i ->
                    val hospital = it[i]
                    viewModel.selectHospital(hospital)
                }.create().show()
            }
        })

        return binding.root
    }


}
