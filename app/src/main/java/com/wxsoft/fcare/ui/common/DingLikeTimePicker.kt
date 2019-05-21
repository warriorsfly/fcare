package com.wxsoft.fcare.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wxsoft.fcare.R
import kotlinx.android.synthetic.main.new_time_picker.*
import kotlinx.android.synthetic.main.new_time_picker.view.*
import java.util.*

class DingLikeTimePicker (private val mills:Long,private val timeSelect:(Long)->Unit): BottomSheetDialogFragment(){
    private lateinit var adapter:DingLikeAdapter


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.new_time_picker,container).apply {


            adapter= DingLikeAdapter(if(mills==0L)System.currentTimeMillis() else mills,::timeChanged)


            viewPager.adapter=adapter
            viewPager.isUserInputEnabled = false
            TabLayoutMediator(tab, viewPager) { tab, position ->
                tab.text = if(position==0)"${adapter.calendar.get(Calendar.YEAR)}-${String.format("%02d",adapter.calendar.get(Calendar.MONTH)+1)}-${String.format("%02d",adapter.calendar.get(Calendar.DAY_OF_MONTH))}"
                else "${String.format("%02d",adapter.calendar.get(Calendar.HOUR_OF_DAY))}:${String.format("%02d",adapter.calendar.get(Calendar.MINUTE))}"
            }.attach()

            tab.selectTab(tab.getTabAt(1))

            submit.setOnClickListener{
                timeSelect(adapter.calendar.timeInMillis)
                dismiss()
            }
        }
    }

    private fun timeChanged(field:Int){
        if(tab==null)return
        when(field){
            Calendar.DATE->{
                tab.getTabAt(0)?.text="${adapter.calendar.get(Calendar.YEAR)}-${String.format("%02d",adapter.calendar.get(Calendar.MONTH)+1)}-${String.format("%02d",adapter.calendar.get(Calendar.DAY_OF_MONTH))}"
                tab.selectTab(tab.getTabAt(1))
            }

            Calendar.HOUR_OF_DAY,Calendar.MINUTE->{
                tab.getTabAt(1)?.text="${String.format("%02d",adapter.calendar.get(Calendar.HOUR_OF_DAY))}:${String.format("%02d",adapter.calendar.get(Calendar.MINUTE))}"
            }

        }
    }
}