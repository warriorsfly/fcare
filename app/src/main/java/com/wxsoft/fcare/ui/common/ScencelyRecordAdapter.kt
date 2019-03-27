package com.wxsoft.fcare.ui.common


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Record
import com.wxsoft.fcare.core.data.entity.rating.RatingResult
import com.wxsoft.fcare.core.data.entity.rating.ScencelyRatingResult
import com.wxsoft.fcare.databinding.ItemRatingScenceBinding


abstract class ScencelyRecordAdapter<T> constructor(private val owner: LifecycleOwner,
                                        private val pool:RecyclerView.RecycledViewPool,
                                        private val newItem: (String)->Unit,
                                        private val showDetail:(Record<T>)->Unit,
                                                callback:ScencelyDiffCallback<T>): ListAdapter<Record<T>,ScencelyRecordAdapter.ItemViewHolder>(callback) {


    class ItemViewHolder(bind: ViewDataBinding,
                         private val pool: RecyclerView.RecycledViewPool,
                         private val newItemClick: (String)->Unit) : RecyclerView.ViewHolder(bind.root) {

        var binding: ViewDataBinding
            private set

        init {
            binding = bind.apply {
                root.findViewById<RecyclerView>(R.id.list).setRecycledViewPool(pool)
                root.findViewById<View>(R.id.add).setOnClickListener {
//                    this.
//                    item?.typeId?.let { newItemClick(it) }
                }
            }
        }
    }
    class ScencelyDiffCallback<T> : DiffUtil.ItemCallback<Record<T>>() {
        override fun areItemsTheSame(oldItem: Record<T>, newItem: Record<T>): Boolean {
            return oldItem.typeId==newItem.typeId && oldItem.items.size==newItem.items.size
        }

        override fun areContentsTheSame(oldItem: Record<T>, newItem: Record<T>): Boolean {

            return oldItem.items==newItem.items && oldItem.items.size==newItem.items.size

        }
    }
}



