package com.wxsoft.fcare.ui.main

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Account
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.NfcUtils
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.databinding.ActivityMainBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.main.fragment.patients.MessageFragment
import com.wxsoft.fcare.ui.main.fragment.patients.PatientsFragment
import com.wxsoft.fcare.ui.main.fragment.profile.UserProfileFragment
import com.wxsoft.fcare.ui.main.fragment.task.TaskFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var gson:Gson

    private var doctor:Boolean=false

    @Inject
    lateinit var sharedPreferenceStorage: SharedPreferenceStorage




    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                viewPager.setCurrentItem(0, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_dashboard -> {
                viewPager.setCurrentItem(1, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notifications -> {
//                message.setText(R.string.title_notifications)
                viewPager.setCurrentItem(2, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_user -> {
                viewPager.setCurrentItem(3, true)
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val account=gson.fromJson(sharedPreferenceStorage.userInfo!!, Account::class.java)
        doctor=!account.deptName.contains("120")
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                //                viewModel=this@MainActivity.viewModel
                navigation.apply {
                    if(doctor){
                        menu.clear()
                        inflateMenu(R.menu.navigation2)
                    }

                    setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
                }

                viewPager.adapter = MainAdapter(supportFragmentManager,doctor)
                if (savedInstanceState == null) {
                    navigation.selectedItemId = R.id.nav_home
                }
                lifecycleOwner = this@MainActivity
            }

    }
}


class MainAdapter(fm:FragmentManager,doctor:Boolean) :
    FragmentPagerAdapter(fm) {

    private val fragments:List<Fragment> by lazyFast {
        if(doctor)listOf(PatientsFragment(),TaskFragment(), MessageFragment(),UserProfileFragment())
        else listOf(TaskFragment(), PatientsFragment(),MessageFragment(),UserProfileFragment())
    }

    override fun getItem(position: Int): Fragment {

        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

}