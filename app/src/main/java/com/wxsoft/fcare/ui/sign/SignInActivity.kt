package com.wxsoft.fcare.ui.sign

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.EntityIdName
import com.wxsoft.fcare.core.data.entity.SignInUser
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityUserSignInBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.utils.ActionCode.Companion.ARG_NEW_ITEM_CODE
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject


class SignInActivity : BaseActivity() {

    companion object {
        const val PATIENT_ID = "PATIENT_ID"

        const val SIGN_IN_DOCTORS=79
    }
    private var selectedSignUser: SignInUser? =null
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: SignInViewModel
    private lateinit var adapter: SignInUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        DataBindingUtil.setContentView<ActivityUserSignInBinding>(
            this,
            R.layout.activity_user_sign_in
        ).apply{
            viewModel=this@SignInActivity.viewModel

            lifecycleOwner = this@SignInActivity
            adapter = SignInUserAdapter(this@SignInActivity,this@SignInActivity::click)
            list.adapter = adapter
        }
        viewModel.data.observe(this, Observer {
            adapter.submitList(it)
        })
        viewModel.getSign()
        setSupportActionBar(toolbar)
        title="签到"
    }

    private fun click(item: SignInUser) {
        selectedSignUser =item
        val intent = Intent(this,SelectSignDoctorActivity::class.java)
        startActivityForResult(intent,SIGN_IN_DOCTORS)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==RESULT_OK) {
            when (requestCode) {
                SIGN_IN_DOCTORS -> {

                    val users = data?.getParcelableExtra<EntityIdName>("doctor")
                    selectedSignUser?.apply {
                        users?.let {
                            viewModel.sign(it.id,shiftsCode)
                        }

                    }


                }
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_new,menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//
//        return  when(item?.itemId){
//            R.id.new_item->{
//                true
//            }
//            else->super.onOptionsItemSelected(item)
//        }
//    }
}
