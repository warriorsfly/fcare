package com.wxsoft.fcare.ui.details.comingby.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityComingByBinding
import com.wxsoft.fcare.databinding.ActivityComingByListBinding
import com.wxsoft.fcare.databinding.ItemEcgDiagnoseTextBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.comingby.ComingByTypeViewModel
import com.wxsoft.fcare.ui.details.comingby.ComingByViewModel
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject
import dagger.android.support.DaggerFragment as DaggerFragment1

class ComingByItemListActivity : BaseActivity() {

    /**
     * 1来院方式2发车单位3绕行急诊科后绕行科室
     */
    private val type by lazyFast {
        intent?.getIntExtra("type",3)?:3
    }

    private val patientId by lazyFast {
        intent?.getStringExtra("PATIENT_ID")?:""
    }
    val iten= Intent()
    private lateinit var viewModel: ComingByTypeViewModel

    @Inject
    lateinit var factory: ViewModelFactory
    lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=viewModelProvider(factory)
        adapter = Adapter(this@ComingByItemListActivity, ::select)

        DataBindingUtil.setContentView<ActivityComingByListBinding>(this, R.layout.activity_coming_by_list).apply {
            lifecycleOwner=this@ComingByItemListActivity
            list.adapter=this@ComingByItemListActivity.adapter
        }
        setSupportActionBar(toolbar)
        title="方式"

        viewModel.comingType.observe(this, Observer {it.let(::loaded)})

        viewModel.patientId=patientId
        viewModel.type=type
    }

    private fun loaded(dictionarys: List<Dictionary>){


        dictionarys.let(adapter::submitList)
    }

    private fun select(dictionary: Dictionary) {
        iten.putExtra("type",type)
        iten.putExtra("id",dictionary.id)
        iten.putExtra("name",dictionary.itemName)
        setResult(Activity.RESULT_OK,iten)
        finish()
    }


    class Adapter constructor(private val owner: LifecycleOwner,private val itemSelected:(Dictionary)->Unit) :
        ListAdapter<Dictionary,Adapter.ItemViewHolder>(DiffCallback) {

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

            holder.binding.apply {
                item=getItem(position)

                executePendingBindings()

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

            val binding =
                ItemEcgDiagnoseTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    .apply {
                        lifecycleOwner = this@Adapter.owner
                        root.setOnClickListener {
                            item?.let(itemSelected)
                        }
                    }
            return ItemViewHolder(binding)
        }

        class ItemViewHolder(val binding: ItemEcgDiagnoseTextBinding) : RecyclerView.ViewHolder(binding.root)


        object DiffCallback : DiffUtil.ItemCallback<Dictionary>() {
            override fun areItemsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {

                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {

                return oldItem.itemName == newItem.itemName && oldItem.checked==newItem.checked
            }
        }

    }
}
