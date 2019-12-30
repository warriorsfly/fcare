package com.wxsoft.fcare.ui.sign

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.EntityIdName
import com.wxsoft.fcare.core.data.entity.User
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityComingByListBinding
import com.wxsoft.fcare.databinding.ItemDoctorTextBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_coming_by_list.*
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class SelectSignDoctorActivity : BaseActivity(), SearchView.OnQueryTextListener {

    private val type by lazyFast {
        intent?.getIntExtra("TYPE",0)?: 0
    }

    private val single by lazyFast {
        intent?.getBooleanExtra("single",true)?: true
    }


    private val ids by lazyFast {
        intent?.getStringArrayExtra("doctorIds")?: emptyArray()
    }

    private lateinit var viewModel: SelectSignDoctorViewModel

    @Inject
    lateinit var factory: ViewModelFactory
    lateinit var adapter: Adapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(!single)
        menuInflater.inflate(R.menu.menu_subject,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{

                viewModel.doctors.value?.let{
                    iten.putParcelableArrayListExtra("doctors",ArrayList<EntityIdName>().apply {
                        addAll(
                            it.filter { it.checked }.map { user ->
                                EntityIdName(
                                    user.id,
                                    user.trueName
                                )
                            }
                        )
                    })
                    setResult(Activity.RESULT_OK,iten)
                    finish()
                    true
                }
                false

            }
            else->super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=viewModelProvider(factory)
//        viewModel.type=type
        adapter = Adapter(this@SelectSignDoctorActivity,::select)
        adapter.singleSelect=single
        DataBindingUtil.setContentView<ActivityComingByListBinding>(this, R.layout.activity_coming_by_list).apply {
            lifecycleOwner=this@SelectSignDoctorActivity
            list.adapter=this@SelectSignDoctorActivity.adapter
            search.setOnQueryTextListener(this@SelectSignDoctorActivity)
        }
//        setSupportActionBar(toolbar)
//        title=if(single)"执行人员" else "操作医生"

        back.setOnClickListener { onBackPressed() }
//        setSupportActionBar(toolbar)
//        title=if(type==0)"医生列表" else "护士列表"
        viewModel.doctors.observe(this, Observer {
            it.let(::loaded)
        })

    }

    private fun loaded(users:List<User>){
        users.forEach {
            if (it.id in ids) {
                it.checked = true
            }
        }
        users.let(adapter::submitList)
    }
    val iten= Intent()
    private fun select(user: User) {
        iten.putExtra("doctor",EntityIdName(user.id,user.trueName))
        setResult(Activity.RESULT_OK,iten)
        finish()
    }

    class Adapter constructor(private val owner: LifecycleOwner,
                              private val itemSelected:(User)->Unit,
                              var singleSelect:Boolean=true) :
        ListAdapter<User,Adapter.ItemViewHolder>(DiffCallback) {

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

            holder.binding.apply {
                item=getItem(position)

                executePendingBindings()

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

            val binding =
                ItemDoctorTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    .apply {
                        lifecycleOwner = this@Adapter.owner
                        root.setOnClickListener {
                            if (singleSelect) {
                                item?.let(itemSelected)
                            }else{
                                item?.let {
                                    it.checked=!it.checked
//                                    it.let(itemSelected)

                                }
                            }
                        }
                    }
            return ItemViewHolder(binding)
        }

        class ItemViewHolder(val binding: ItemDoctorTextBinding) : RecyclerView.ViewHolder(binding.root)


        object DiffCallback : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {

                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {

                return oldItem.trueName == newItem.trueName && oldItem.checked==newItem.checked
            }
        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.filterString = newText
        return true
    }
}