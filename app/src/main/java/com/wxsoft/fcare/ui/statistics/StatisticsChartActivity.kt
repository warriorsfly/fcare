package com.wxsoft.fcare.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityStatisticsChartBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_statistics_chart.*
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject


class StatisticsChartActivity : BaseActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: StatisticsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_chart)

        viewModel = viewModelProvider(factory)
        DataBindingUtil.setContentView<ActivityStatisticsChartBinding>(this, R.layout.activity_statistics_chart)
            .apply {
                viewModel = this@StatisticsChartActivity.viewModel
                lifecycleOwner = this@StatisticsChartActivity
            }
        setSupportActionBar(toolbar)

        val colors =  ArrayList<Int>();
        colors.add(resources.getColor(R.color.light_green))
//        colors.add(resources.getColor(R.color.colorPrimaryDark))
        colors.add(resources.getColor(R.color.bg_light_gray))

        viewModel.data.observe(this, Observer {

            val d= mutableListOf(it.st2*100/it.st1.toFloat(),(it.st1-it.st2)*100/it.st1.toFloat()).mapIndexed { index, f ->  PieEntry(f,viewModel.pie1s[index]) }
            val ds = PieDataSet(d,"")
            ds.colors=colors
            val data1 = PieData(ds)
            data1.setValueFormatter(PercentFormatter());
            data1.setValueTextSize(14f);
            pie1.data =data1
            pie1.description = Description().apply {
                text="绿道人数/门诊量"
            }


            val d2= mutableListOf(it.st3*100/it.st2.toFloat(),(it.st2-it.st3)*100/it.st2.toFloat()).mapIndexed { index, f ->  PieEntry(f,viewModel.pie2s[index]) }
            val ds2 = PieDataSet(d2,"")
            ds2.colors=colors
            val data2 = PieData(ds2)
            data2.setValueFormatter(PercentFormatter());
            data2.setValueTextSize(14f);
            pie2.data =data2
            pie2.description = Description().apply {
                text="溶栓占绿色通道百分比"
            }

            val d3= mutableListOf(it.st3*100/it.st1.toFloat(),(it.st1-it.st3)*100/it.st1.toFloat()).mapIndexed { index, f ->  PieEntry(f,viewModel.pie2s[index]) }
            val ds3 = PieDataSet(d3,"")
            ds3.colors=colors
            val data3 = PieData(ds3)
            data3.setValueFormatter(PercentFormatter());
            data3.setValueTextSize(14f);
            pie3.data =data3
            pie3.description = Description().apply {
                text="溶栓占急诊量百分比"
            }

            val d4= mutableListOf(it.st3*100/it.st2.toFloat(),(it.st2-it.st3)*100/it.st2.toFloat()).mapIndexed { index, f ->  PieEntry(f,viewModel.pie3s[index]) }
            val ds4 = PieDataSet(d4,"")
            ds4.colors=colors
            val data4 = PieData(ds4)
            data4.setValueFormatter(PercentFormatter());
            data4.setValueTextSize(14f);
            pie4.data =data4
            pie4.description = Description().apply {
                text="取栓占绿色通道百分比"
            }
        })

        title="统计"

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_subject,menu)
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{

                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

}



