package com.wxsoft.fcare.ui.main

import android.content.Intent
import android.databinding.DataBindingUtil
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.wxsoft.emergency.ui.main.fragment.patients.PatientsFragment
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityMainBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.main.fragment.task.TaskFragment
import com.wxsoft.fcare.ui.main.fragment.profile.UserProfileFragment
import com.wxsoft.fcare.utils.NfcUtils
import com.wxsoft.fcare.utils.inTransaction
import com.wxsoft.fcare.utils.viewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MainActivity : BaseActivity() {

    companion object {
        private const val FRAGMENT_ID = R.id.fragment_container
    }

    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityMainBinding

    lateinit var viewModel: MainViewModel

    private lateinit var fragments:List<DaggerFragment>

    private var nfcAdapter: NfcAdapter? = null


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
//                message.setText(R.string.title_home)

                replaceFragment(fragments[0])
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_dashboard -> {
               replaceFragment(fragments[1])
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notifications -> {
//                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_user -> {
                replaceFragment(fragments[2])
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }


    private fun <F> replaceFragment(fragment: F) where F : DaggerFragment {
        supportFragmentManager.inTransaction {
            replace(FRAGMENT_ID, fragment)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(factory)

        fragments= listOf(TaskFragment(),PatientsFragment(),UserProfileFragment())

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                viewModel=this@MainActivity.viewModel
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
                if (savedInstanceState == null) {
                    navigation.selectedItemId = R.id.nav_home
                }
                setLifecycleOwner(this@MainActivity)
            }



        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
//        pi = PendingIntent.getActivity(
//            this, 0, Intent(this, javaClass)
//                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
//        )
    }

    override fun onResume() {
        super.onResume();
//        nfcAdapter?.enableForegroundDispatch(this, pi, null, null); //启动
    }

    override fun onPause() {
        super.onPause()

//        nfcAdapter?.disableForegroundDispatch(this); //启动
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent!!.action) {

            val tagFromIntent = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            val cardId = NfcUtils.toHexString(tagFromIntent.id)

//            viewModel.loadByRfid(cardId)
        }
    }

}
