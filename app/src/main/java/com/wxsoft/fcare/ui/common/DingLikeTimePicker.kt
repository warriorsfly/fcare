package com.wxsoft.fcare.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.wxsoft.fcare.R
import java.util.*

class DingLikeTimePicker (private val mills:Long): BottomSheetDialogFragment(){
    private lateinit var adapter:DingLikeAdapter
    private lateinit var tab:TabLayout
    private lateinit var viewPager:ViewPager2

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.new_time_picker,container).apply {
            viewPager=findViewById(R.id.viewPager)
            tab=findViewById(R.id.tab)
            adapter= DingLikeAdapter(if(mills==0L)System.currentTimeMillis() else mills,::timeChanged)
            viewPager.adapter=adapter
            TabLayoutMediator(tab, viewPager) { tab, position ->
                tab.text = if(position==0)"${adapter.calendar.get(Calendar.YEAR)}-${String.format("%02d",adapter.calendar.get(Calendar.MONTH))}-${String.format("%02d",adapter.calendar.get(Calendar.DAY_OF_MONTH))}"
                else "${String.format("%02d",adapter.calendar.get(Calendar.HOUR))}:${String.format("%02d",adapter.calendar.get(Calendar.MINUTE))}"
            }.attach()
        }
    }

    private fun timeChanged(field:Int){
        when(field){
            Calendar.YEAR,Calendar.MONTH,Calendar.DATE->{
                tab.getTabAt(0)?.text="${adapter.calendar.get(Calendar.YEAR)}-${String.format("%02d",adapter.calendar.get(Calendar.MONTH))}-${String.format("%02d",adapter.calendar.get(Calendar.DAY_OF_MONTH))}"
            }

            Calendar.HOUR,Calendar.MINUTE->{
                tab.getTabAt(1)?.text="${String.format("%02d",adapter.calendar.get(Calendar.HOUR))}:${String.format("%02d",adapter.calendar.get(Calendar.MINUTE))}"
            }

        }
    }
}