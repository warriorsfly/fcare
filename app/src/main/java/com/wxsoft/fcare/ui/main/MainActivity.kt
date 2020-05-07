package com.wxsoft.fcare.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Account
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.databinding.ActivityMainBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.main.fragment.patients.MessageFragment
import com.wxsoft.fcare.ui.main.fragment.patients.PatientsFragment
import com.wxsoft.fcare.ui.main.fragment.profile.UserProfileFragment
import com.wxsoft.fcare.ui.main.fragment.task.TaskFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity() ,MessageFragment.CallBackValue{

    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var gson:Gson

    private var doctor:Boolean=false

    private lateinit var itemMessageMemu: BadgeDrawable

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
        if (!account.deptId.isNullOrEmpty()) doctor=account.deptId.contains("0")
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
                this@MainActivity.itemMessageMemu = navigation.getOrCreateBadge(R.id.nav_notifications)
                this@MainActivity.itemMessageMemu.isVisible = false
                viewPager.adapter = MainAdapter(supportFragmentManager,doctor)
                if (savedInstanceState == null) {
                    navigation.selectedItemId = R.id.nav_home
                }
                lifecycleOwner = this@MainActivity
            }

    }

    override fun setMemuCount(count: Int) {
        itemMessageMemu.isVisible = (count!=0)
        itemMessageMemu.number = count

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