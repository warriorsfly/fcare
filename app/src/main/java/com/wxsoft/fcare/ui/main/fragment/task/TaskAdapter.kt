package com.wxsoft.fcare.ui.main.fragment.task

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.databinding.LayoutItemAssignmentBinding
import kotlinx.android.synthetic.main.layout_item_assignment.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


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
            var task  = differ.currentList[position]
            var patients = differ.currentList[position].patients
            var names = ""
            patients.map {
                names = if (names.equals("")) it.name else names+"  "+it.name
            }
            if (patients.size>0){
                differ.currentList[position].taskPosition = patients[0].attackPosition
            }
            if ((task.startAt!=null) && (task.arriveHosAt != null)){
               root.all_time.setText(getTimeDifference(task.startAt!!,task.arriveHosAt!!))
            }
            when(task.status){
                1->root.status_image.setImageResource(R.drawable.ic_task_status_start)
                2->root.status_image.setImageResource(R.drawable.ic_task_status_arrived)
                3->root.status_image.setImageResource(R.drawable.ic_task_status_tuch_patient)
                4->root.status_image.setImageResource(R.drawable.ic_task_status_back)
                5->root.status_image.setImageResource(R.drawable.ic_task_status_end)
            }
            root.parient_name.setText(names)
            setVariable(BR.task, differ.currentList[position])
            setVariable(BR.listener, viewModel)
            setLifecycleOwner(lifecycleOwner)
            executePendingBindings()

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            LayoutItemAssignmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
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

    fun getTimeDifference(starTime:String , endTime:String ):String{
        var timeString = ""
        var dateFormat =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            var parse = dateFormat.parse(starTime);
            var parse1 = dateFormat.parse(endTime);

            var diff = parse1.getTime() - parse.getTime();

            var day = diff / (24 * 60 * 60 * 1000);
            var hour = (diff / (60 * 60 * 1000) - day * 24);
            var min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            var s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            var ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                    - min * 60 * 1000 - s * 1000);
            // System.out.println(day + "天" + hour + "小时" + min + "分" + s +
            // "秒");
            var hour1 = diff / (60 * 60 * 1000);
            var hourString = hour1.toString()
            var min1 = ((diff / (60 * 1000)) - hour1 * 60);
            timeString = hour1.toString() + "小时" + min1 + "分";
            // System.out.println(day + "天" + hour + "小时" + min + "分" + s +
            // "秒");
        }catch (e: ParseException){
            e.printStackTrace()
        }
        return timeString;

    }


}