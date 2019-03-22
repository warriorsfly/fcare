package com.wxsoft.fcare.ui.main.fragment.task

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.DateTimeUtils.Companion.formatter
import com.wxsoft.fcare.databinding.LayoutItemAssignmentBinding
import java.text.ParseException


class TaskAdapter constructor(private val owner: LifecycleOwner, val viewModel: TaskViewModel) :
    ListAdapter<Task,TaskAdapter.ItemViewHolder>(DiffCallback){



    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            var item = getItem(position)
            task = item
            listener = viewModel
            if (item.startAt!=null&&item.arriveHosAt!=null){
                var startTime = DateTimeUtils.formatter.parse(item.startAt)?.time!!
                var endTime = DateTimeUtils.formatter.parse(item.arriveHosAt)?.time!!
                var fireAt=(endTime - startTime)/1000
                var minute = (fireAt/60).toString()
                var second = (fireAt%60).toString()
                allTime.setText(minute +"分钟"+second+"秒")
            }
            if (patientsList.adapter == null) {
                val adapter = TaskPatientsItemAdapter(owner,viewModel)
                patientsList.adapter = adapter
            }

            (patientsList.adapter as? TaskPatientsItemAdapter)?.submitList(item.patients)
            executePendingBindings()

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = LayoutItemAssignmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .apply {
                lifecycleOwner = owner
            }
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

            return oldItem.id == newItem.id  && oldItem.patients == newItem.patients
        }
    }

    private fun getTimeDifference(starTime:String, endTime:String ):String{
        var timeString = ""

        try {
            val parse = formatter.parse(starTime)
            val parse1 = formatter.parse(endTime)

            val diff = parse1.time - parse.time
            // System.out.println(day + "天" + hour + "小时" + min + "分" + s +
            // "秒")
            val hour1 = diff / (60 * 60 * 1000)

            val min1 = ((diff / (60 * 1000)) - hour1 * 60)
            timeString = hour1.toString() + "小时" + min1 + "分"

        }catch (e: ParseException){
            e.printStackTrace()
        }
        return timeString

    }


}