package com.wxsoft.fcare.ui.main

import android.content.Intent
import android.databinding.DataBindingUtil
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityMainBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.main.fragment.patients.PatientsFragment
import com.wxsoft.fcare.ui.main.fragment.profile.UserProfileFragment
import com.wxsoft.fcare.ui.main.fragment.task.TaskFragment
import com.wxsoft.fcare.core.utils.NfcUtils
import com.wxsoft.fcare.core.utils.lazyFast
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    private var nfcAdapter: NfcAdapter? = null


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
//                message.setText(R.string.title_home)
                viewPager.setCurrentItem(0,true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_dashboard -> {
                viewPager.setCurrentItem(1,true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notifications -> {
//                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_user -> {
                viewPager.setCurrentItem(2,true)
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
//                viewModel=this@MainActivity.viewModel
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
                if (savedInstanceState == null) {
                    navigation.selectedItemId = R.id.nav_home
                }
                lifecycleOwner = this@MainActivity
            }


        viewPager.adapter = MainAdapter(supportFragmentManager)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent!!.action) {

            val tagFromIntent = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            val cardId = NfcUtils.toHexString(tagFromIntent.id)

//            viewModel.loadByRfid(cardId)
        }
    }

}


class MainAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    private val fragments:List<Fragment> by lazyFast {
        listOf(TaskFragment(), PatientsFragment(),UserProfileFragment())
    }

    override fun getItem(position: Int): Fragment {

        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

}