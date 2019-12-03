package com.wxsoft.fcare.ui.details.catheter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
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
import com.wxsoft.fcare.databinding.ActivityCatheterDoctorsBinding
import com.wxsoft.fcare.databinding.ActivityComingByDoctorsBinding
import com.wxsoft.fcare.databinding.ItemDoctorTextBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.comingby.ComingByDoctorsViewModel
import javax.inject.Inject

class CatheterDoctorsActivity : BaseActivity() , SearchView.OnQueryTextListener{
    override fun onQueryTextSubmit(p0: String?): Boolean {
        return viewModel.showPatients(if(p0.isNullOrEmpty())"" else p0)
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        if(p0.isNullOrEmpty()){
            viewModel.showPatients("")
            return true
        }
        return false
    }

    /**
     *
     */
    private val type by lazyFast {
        intent?.getIntExtra("type",1)?:1
    }

    private val patientId by lazyFast {
        intent?.getStringExtra("PATIENT_ID")?:""
    }

    private val ids by lazyFast {
        intent?.getParcelableArrayListExtra<EntityIdName>("ids")?.map { it.id }?: emptyList()
    }

    private lateinit var viewModel: CatheterDoctorsViewModel

    @Inject
    lateinit var factory: ViewModelFactory
    lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel=viewModelProvider(factory)
        viewModel.patientId=patientId
        viewModel.type=type
        adapter = Adapter(this@CatheterDoctorsActivity,::select)
        adapter.singleSelect=type!=3
        DataBindingUtil.setContentView<ActivityCatheterDoctorsBinding>(this, R.layout.activity_catheter_doctors).apply {
            lifecycleOwner=this@CatheterDoctorsActivity
            back.setOnClickListener { onBackPressed() }
            list.adapter=this@CatheterDoctorsActivity.adapter
            viewModel = this@CatheterDoctorsActivity.viewModel
            search.setOnQueryTextListener(this@CatheterDoctorsActivity)
        }
        viewModel.emergencyDoctors.observe(this, Observer {
            it.let(::loaded)
        })
        viewModel.consultantDoctors.observe(this, Observer {
            it.let(::loaded)
        })

    }

    private fun loaded(users:List<User>){
        users.forEach {
            if (it.userName in ids) {
                it.checked = true
            }
        }
        users.let(adapter::submitList)
    }
    val iten= Intent()
    private fun select(user: User) {

        iten.putExtra("type",type)
        when(type){

            1->
            {
                iten.putParcelableArrayListExtra("user", arrayListOf(EntityIdName(user.userName,user.trueName)))
                setResult(Activity.RESULT_OK,iten)
                finish()
            }
            2->{
                iten.putParcelableArrayListExtra("user", arrayListOf(EntityIdName(user.userName,user.trueName)))
                setResult(Activity.RESULT_OK,iten)
                finish()}

        }
    }

    override fun onBackPressed() {

        if(type==3){
            val lis=viewModel.consultantDoctors.value?.
                filter { it.checked }?.
                map {  EntityIdName(it.userName,it.trueName) }?: emptyList()
            val li=ArrayList<EntityIdName>().apply{
                addAll(lis)
            }

            iten.putParcelableArrayListExtra("user",li )
            setResult(Activity.RESULT_OK,iten)
            finish()
        }else {
            super.onBackPressed()
        }
    }

    class Adapter constructor(private val owner: LifecycleOwner,
                              private val itemSelected:(User)->Unit,
                              var singleSelect:Boolean=true) :
        ListAdapter<User, Adapter.ItemViewHolder>(DiffCallback) {

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
                                    it.let(itemSelected)

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
}
