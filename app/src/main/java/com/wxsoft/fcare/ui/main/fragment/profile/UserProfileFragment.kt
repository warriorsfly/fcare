package com.wxsoft.fcare.ui.main.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

            logout.setOnClickListener {
                viewModel.loginOut()
                val intent = Intent(activity!!, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(intent)
            }
            lifecycleOwner = this@UserProfileFragment
        }

        return binding.root
    }


}
