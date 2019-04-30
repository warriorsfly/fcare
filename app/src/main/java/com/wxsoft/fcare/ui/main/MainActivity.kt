package com.wxsoft.fcare.ui.main

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
    lateinit var account:Account
    @Inject
    lateinit var sharedPreferenceStorage: SharedPreferenceStorage

//    private var nfcAdapter: NfcAdapter? = null


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val doctor=account.deptName.contains("120")
        when (item.itemId) {
            R.id.nav_home -> {
                viewPager.setCurrentItem(if(doctor)1 else 0, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_dashboard -> {
                viewPager.setCurrentItem(if(doctor)0 else 1, true)
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
        account=gson.fromJson(sharedPreferenceStorage.userInfo!!, Account::class.java)

        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                //                viewModel=this@MainActivity.viewModel
                navigation.apply {
                    if(account.deptName.contains("120")){
                        menu.clear()
                        inflateMenu(R.menu.navigation2)
                    }

                    setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//                    for ( v in getChildAt(2).fin)
//                    getChildAt(2) .findViewById<TextView>(R.id.smallLabel).visibility= View.GONE
//                    getChildAt(2) .findViewById<TextView>(R.id.largeLabel).visibility= View.GONE
                }

                viewPager.adapter = MainAdapter(supportFragmentManager,account.deptName.contains("120"))
                if (savedInstanceState == null) {
                    if(account.deptName.contains("120"))
                        navigation.selectedItemId = R.id.nav_dashboard
                        else
                        navigation.selectedItemId = R.id.nav_home
                }
                lifecycleOwner = this@MainActivity
            }



//        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
//        pi = PendingIntent.getActivity(
//            this, 0, Intent(this, javaClass)
//                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
//        )
    }

//    override fun onResume() {
//        super.onResume();
////        nfcAdapter?.enableForegroundDispatch(this, pi, null, null); //启动
//    }
//
//    override fun onPause() {
//        super.onPause()
//
////        nfcAdapter?.disableForegroundDispatch(this); //启动
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//
//    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//
//        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent!!.action) {
//
//            val tagFromIntent = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
////            val cardId = NfcUtils.toHexString(tagFromIntent.id)
//
////            viewModel.loadByRfid(cardId)
//        }
//    }
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