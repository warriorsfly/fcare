package com.wxsoft.fcare.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.recyclerview.widget.RecyclerView
import com.bigkoo.pickerview.adapter.NumericWheelAdapter
import com.contrarywind.view.WheelView
import com.wxsoft.fcare.R
import java.util.*

class DingLikeAdapter(private var timeMillis:Long,private val timeChanged:(Int)->Unit):RecyclerView.Adapter<DingLikeAdapter.ItemViewHolder>(){
    val calendar:Calendar = Calendar.getInstance().apply {
        timeInMillis=timeMillis
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0->{
                R.layout.new_picker_calendar
            }
            else->{
                R.layout.new_picker_hour_second
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater= LayoutInflater.from (parent.context)

        return when(viewType){
            R.layout.new_picker_calendar->{
                ItemViewHolder.DateViewHolder(inflater.inflate(viewType,parent,false)).apply {
                    val calendarView=view.findViewById<CalendarView>(R.id.calendar)
                    calendarView.setOnDateChangeListener{ _, year, month, dayOfMonth ->
                        calendar.set(year,month,dayOfMonth)
                        timeChanged(Calendar.DATE)
                    }
                }
            }
            else->{

                ItemViewHolder.TimeViewHolder(inflater.inflate(viewType,parent,false)).apply {
                    view.findViewById<WheelView>(R.id.hour).apply {
                        adapter= NumericWheelAdapter(0,23)
                        setTextSize(32f)
                        setOnItemSelectedListener{
                            calendar.set(Calendar.HOUR_OF_DAY,it)
                            timeChanged(Calendar.HOUR_OF_DAY)
                        }
                    }
                    view.findViewById<WheelView>(R.id.minute).apply {
                        adapter=NumericWheelAdapter(0,59)
                        setTextSize(32f)
                        setOnItemSelectedListener{
                            calendar.set(Calendar.MINUTE,it)
                            timeChanged(Calendar.MINUTE)
                        }
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when(holder){
            is ItemViewHolder.DateViewHolder->{
                val calendarView=holder.view.findViewById<CalendarView>(R.id.calendar)
                calendarView.date = calendar.timeInMillis
            }
            is ItemViewHolder.TimeViewHolder->{
                val hourView=holder.view.findViewById<WheelView>(R.id.hour)
                hourView.currentItem=calendar.get(Calendar.HOUR)
                val minuteView=holder.view.findViewById<WheelView>(R.id.minute)
                minuteView.currentItem=calendar.get(Calendar.MINUTE)
            }
        }
    }


    sealed class ItemViewHolder( val view:View):RecyclerView.ViewHolder(view){
        class DateViewHolder(view: View):ItemViewHolder(view)
        class TimeViewHolder(view: View):ItemViewHolder(view)
    }
}

