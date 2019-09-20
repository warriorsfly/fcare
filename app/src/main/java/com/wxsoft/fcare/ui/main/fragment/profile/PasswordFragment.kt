package com.wxsoft.fcare.ui.main.fragment.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentPasswordBinding
import com.wxsoft.fcare.ui.login.LoginActivity
import com.wxsoft.fcare.widget.CustomDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class PasswordFragment : CustomDimDialogFragment(), HasSupportFragmentInjector {


    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: UserProfileViewModel
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>


    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen)
        viewModel = activityViewModelProvider(factory)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewModel = activityViewModelProvider(factory)
        viewModel.passChanged.observe(this, Observer {
            if(it){
                Toast.makeText(context,"密码修改成功",Toast.LENGTH_SHORT).show()
                val intent = Intent(activity!!, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(intent)
                activity?.finish()
            }
        })
        val binding = FragmentPasswordBinding.inflate(inflater, container, false).apply {

            submit.setOnClickListener {
               val oldp=oldPassword.text.toString()
               val newp=newPassword.text.toString()
               val newp2=newPassword2.text.toString()

                if(!viewModel.checkOldPassWord(oldp)){
                    Toast.makeText(context,"密码不对",Toast.LENGTH_SHORT).show()
                    oldPassword.selectAll()
                }else if(newp!=newp2){
                    Toast.makeText(context,"两次输入的新密码不同",Toast.LENGTH_SHORT).show()
                    newPassword2.selectAll()
                }else if(newp.length<6){
                    Toast.makeText(context,"新密码长度少于6位",Toast.LENGTH_SHORT).show()
                    newPassword.selectAll()
                }else{
                    viewModel.changePassword(oldp,newp)
                }
            }
            cancel.setOnClickListener {
                dismiss()
            }
            lifecycleOwner = this@PasswordFragment
        }

        return binding.root
    }


}
