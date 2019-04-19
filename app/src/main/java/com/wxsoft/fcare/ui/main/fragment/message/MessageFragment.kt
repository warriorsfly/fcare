package com.wxsoft.fcare.ui.main.fragment.patients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.LayoutListBinding
import com.wxsoft.fcare.ui.main.fragment.message.MessageAdapter
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
