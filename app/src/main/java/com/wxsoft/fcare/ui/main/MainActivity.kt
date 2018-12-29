package com.wxsoft.fcare.ui.main

import android.app.PendingIntent
import android.content.Intent
import android.databinding.DataBindingUtil
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityMainBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.main.fragment.assignment.AssignmentFragment
import com.wxsoft.fcare.ui.main.fragment.profile.UserProfileFragment
import com.wxsoft.fcare.utils.NfcUtils
import com.wxsoft.fcare.utils.inTransaction
import com.wxsoft.fcare.utils.viewModelProvider
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    companion object {
        private const val FRAGMENT_ID = R.id.fragment_container
    }

    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityMainBinding

    lateinit var viewModel: MainViewModel

    private var nfcAdapter: NfcAdapter? = null


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
//                message.setText(R.string.title_home)

                replaceFragment(AssignmentFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_dashboard -> {
//                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notifications -> {
//                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_user -> {
                replaceFragment(UserProfileFragment())
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

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                setLifecycleOwner(this@MainActivity)
            }


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        binding.viewModel = viewModel

        if (savedInstanceState == null) {
            navigation.selectedItemId = R.id.nav_home
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
