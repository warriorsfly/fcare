package com.wxsoft.fcare.ui.rating

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentRatingsSheetBinding
import com.wxsoft.fcare.databinding.ItemRatingSheetBinding
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class RatingsSheetFragment constructor( private val itemClick: (Rating) -> Unit) : BottomSheetDialogFragment() , HasSupportFragmentInjector {

    companion object {
        const val TAG="RatingsSheetFragment"
    }
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    var patientId=""
    private lateinit var viewModel: RatingViewModel
    private lateinit var adapter: ItemAdapter

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = activityViewModelProvider(factory)
        if(viewModel.patientId.isEmpty()){
            viewModel.patientId=patientId
        }
        adapter=ItemAdapter(this@RatingsSheetFragment,itemClick)
        viewModel.ratings.observe(this, Observer {
            adapter.submitList(it)
        })
        return FragmentRatingsSheetBinding.inflate(inflater,container,false).apply {
            list.adapter=this@RatingsSheetFragment.adapter
            canncel.setOnClickListener { this@RatingsSheetFragment.dismiss() }
            lifecycleOwner=this@RatingsSheetFragment
        }.root
    }



    private class ItemAdapter constructor(private val owner: LifecycleOwner,private  val itemClick:(Rating)->Unit) :
        ListAdapter<Rating,ItemAdapter.ItemViewHolder>(DiffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val inflater=LayoutInflater.from(parent.context)
            return ItemViewHolder(ItemRatingSheetBinding.inflate(inflater,parent,false) , itemClick)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
           holder.binding.apply {
               item = getItem(position)
               lifecycleOwner=owner
               executePendingBindings()
           }

        }

        object DiffCallback : DiffUtil.ItemCallback<Rating>() {
            override fun areItemsTheSame(oldItem: Rating, newItem: Rating): Boolean {
                return oldItem.id==newItem.id
            }

            override fun areContentsTheSame(oldItem: Rating, newItem: Rating): Boolean {
                return oldItem.id==newItem.id
            }
        }

        class ItemViewHolder(bind: ItemRatingSheetBinding,itemClick:(Rating)->Unit) : RecyclerView.ViewHolder(bind.root) {

            var binding: ItemRatingSheetBinding
                private set

            init {
                this.binding = bind.apply {
                    root.setOnClickListener {
                        item?.let{itemClick(it)}
                    }
                }


            }

        }
    }
}
