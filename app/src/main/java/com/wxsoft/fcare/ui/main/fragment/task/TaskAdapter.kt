package com.wxsoft.fcare.ui.main.fragment.task

import android.arch.lifecycle.LifecycleOwner
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.databinding.LayoutItemAssignmentBinding
import com.wxsoft.fcare.utils.DateTimeUtils.Companion.formatter
import java.text.ParseException


class TaskAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: TaskViewModel) :
    RecyclerView.Adapter<TaskAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<Task>(this, DiffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var tasks: List<Task> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val item = differ.currentList[position]
            val patients = differ.currentList[position].patients
            val names = patients.joinToString(separator = " ", transform = { it.name })
            if (patients.isNotEmpty()) {
                differ.currentList[position].taskPosition = patients[0].attackPosition
            }
            if ((item.startAt != null) && (item.arriveHosAt != null)) {
                allTime.text = getTimeDifference(item.startAt!!, item.arriveHosAt!!)
            }
            statusImage.setImageResource(
                when (val status=item.status) {

                    1 -> R.drawable.ic_task_status_start
                    2 -> R.drawable.ic_task_status_arrived
                    3 -> R.drawable.ic_task_status_tuch_patient
                    4 -> R.drawable.ic_task_status_back
                    5 -> R.drawable.ic_task_status_end
                    else -> throw IllegalStateException("Unknown status $status")
                }
            )
            parientName.text = names
            task = item
            listener = viewModel
            setLifecycleOwner(lifecycleOwner)
            executePendingBindings()

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = LayoutItemAssignmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: LayoutItemAssignmentBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: LayoutItemAssignmentBinding
            private set

        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {

            return oldItem.id == newItem.id
        }
    }

    private fun getTimeDifference(starTime:String, endTime:String ):String{
        var timeString = ""

        try {
            var parse = formatter.parse(starTime);
            var parse1 = formatter.parse(endTime);

            var diff = parse1.time - parse.time;

            var day = diff / (24 * 60 * 60 * 1000);
            var hour = (diff / (60 * 60 * 1000) - day * 24);
            var min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            var s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            var ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                    - min * 60 * 1000 - s * 1000);
            // System.out.println(day + "天" + hour + "小时" + min + "分" + s +
            // "秒");
            var hour1 = diff / (60 * 60 * 1000);

            var min1 = ((diff / (60 * 1000)) - hour1 * 60);
            timeString = hour1.toString() + "小时" + min1 + "分";

        }catch (e: ParseException){
            e.printStackTrace()
        }
        return timeString;

    }


}