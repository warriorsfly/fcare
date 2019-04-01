package com.wxsoft.fcare.ui.main.fragment.patients

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.widget.PopupWindowCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentPatientsBinding
import com.wxsoft.fcare.databinding.LayoutListBinding
import com.wxsoft.fcare.databinding.LayoutPatientSelectDateBinding
import com.wxsoft.fcare.databinding.LayoutPatientSelectTypeBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.main.fragment.message.MessageAdapter
import com.wxsoft.fcare.ui.main.fragment.patients.searchpatients.SearchPatientsActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.workspace.WorkingActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class MessageFragment : DaggerFragment() {


    private lateinit var viewModel: MessageViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var adapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activityViewModelProvider(viewModelFactory)

        adapter= MessageAdapter(this)
        return LayoutListBinding.inflate(inflater, container, false).apply {
            list.adapter=this@MessageFragment.adapter
            list.layoutManager=LinearLayoutManager(this@MessageFragment.context)

            val dividerItemDecoration = DividerItemDecoration(
                list.context,
                RecyclerView.VERTICAL
            )
            list.addItemDecoration(dividerItemDecoration)
            viewModel?.messages?.observe(this@MessageFragment, Observer {
                this@MessageFragment.adapter.submitList(it)
            })
        }.root
    }
}
