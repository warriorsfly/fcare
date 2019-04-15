package com.wxsoft.fcare.ui.share

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityComplicationBinding
import com.wxsoft.fcare.databinding.ActivityShareItemListBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class ShareItemListActivity : BaseActivity() {

    private lateinit var patientId:String
    private lateinit var adapter:ShareItemAdapter
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel:ShareItemListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        adapter= ShareItemAdapter(this)
        viewModel.items.observe(this, Observer {
            adapter.submitList(it)
        })
        DataBindingUtil.setContentView<ActivityShareItemListBinding>(this, R.layout.activity_share_item_list)
            .apply {
                list.adapter = adapter
                lifecycleOwner = this@ShareItemListActivity
            }

        setSupportActionBar(toolbar)
        title="分享内容"
        patientId=intent.getStringExtra(PATIENT_ID)?:""

        viewModel.loadItems()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_subject,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}
