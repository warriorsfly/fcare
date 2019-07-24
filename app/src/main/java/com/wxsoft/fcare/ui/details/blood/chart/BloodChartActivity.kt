package com.wxsoft.fcare.ui.details.blood.chart

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.databinding.DataBindingUtil
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.VitalSignsCollectResult
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityBloodChartBinding
import com.wxsoft.fcare.ui.BaseTimingActivity
import com.wxsoft.fcare.ui.details.blood.BloodActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class BloodChartActivity : BaseTimingActivity() {
    override fun selectTime(mills: Long) {

    }

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }
    private lateinit var viewModel: BloodChartViewModel
    private lateinit var lineChart: LineChart
    @Inject
    lateinit var factory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        DataBindingUtil.setContentView<ActivityBloodChartBinding>(this, R.layout.activity_blood_chart)
            .apply {
                viewModel = this@BloodChartActivity. viewModel
                lifecycleOwner = this@BloodChartActivity
            }
        patientId=intent.getStringExtra(BloodActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId


        setSupportActionBar(toolbar)
        title=""

        lineChart = findViewById<LineChart>(R.id.lineChart)

        viewModel.vitalSigns.observe(this, Observer {
            initLineChart(it)
        })

    }


    /**
     * 初始化曲线图表
     *
     * @param list 数据集
     */
    private fun initLineChart(list: List<VitalSignsCollectResult> )
    {
        //无数据时显示的文字
        lineChart.setNoDataText("暂无数据")
        //显示边界
        lineChart.setDrawBorders(false);
        //设置数据
        val entries = ArrayList<Entry>()
        val yDatas:ArrayList<Int> = arrayListOf()
        for (i in list.indices) {
            if (list.get(i).heart_Rate != 0){
                entries.add( Entry(i.toFloat(), list.get(i).heart_Rate.toFloat()))
                yDatas.add(list.get(i).heart_Rate)
            }
        }
        if(entries.size==0) return
        //一个LineDataSet就是一条线
        val lineDataSet = LineDataSet(entries, "心率");
        //线颜色
        lineDataSet.setColor(Color.parseColor("#63B0F8"));
        //线宽度
        lineDataSet.setLineWidth(1.6f)
        //不显示圆点
        lineDataSet.setDrawCircles(false)
        //线条平滑
        lineDataSet.setMode(LineDataSet.Mode.LINEAR)
        //设置折线图填充
//        lineDataSet.setDrawFilled(true)
        val data = LineData(lineDataSet)
        //折线图不显示数值
        data.setDrawValues(false)
        //得到X轴
        val xAxis = lineChart.getXAxis();
        //设置X轴的位置（默认在上方)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴坐标之间的最小间隔
        xAxis.setGranularity(1f);
        //设置X轴的刻度数量，第二个参数为true,将会画出明确数量（带有小数点），但是可能值导致不均匀，默认（6，false）
        xAxis.setLabelCount(yDatas.size, false);
        //设置X轴的值（最小值、最大值、然后会根据设置的刻度数量自动分配刻度显示）
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(yDatas.size.toFloat()-1);
        //不显示网格线
        xAxis.setDrawGridLines(false);
        // 标签倾斜
        xAxis.setLabelRotationAngle(0f);
        //设置X轴值为字符串
        xAxis.setValueFormatter(object :IAxisValueFormatter{
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {

                return list.get(value.toInt()).strTime
            }
        })
        //得到Y轴
        val yAxis = lineChart.getAxisLeft();
        val rightYAxis = lineChart.getAxisRight();
        //设置Y轴是否显示
        rightYAxis.setEnabled(false); //右侧Y轴不显示
        //设置y轴坐标之间的最小间隔
        //不显示网格线
        yAxis.setDrawGridLines(false);
        //设置Y轴坐标之间的最小间隔
        yAxis.setGranularity(10f);
        //设置y轴的刻度数量
        //+2：最大值n就有n+1个刻度，在加上y轴多一个单位长度，为了好看，so+2
        yAxis.setLabelCount(Collections.max(yDatas) + 2, false);
        //设置从Y轴值
        yAxis.setAxisMinimum(Collections.min(yDatas) - 10f);
        //+1:y轴多一个单位长度，为了好看
        yAxis.setAxisMaximum(Collections.max(yDatas) + 10f);
        //y轴
//        yAxis.setValueFormatter(object: IAxisValueFormatter{
//            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
//                return ""
//            }
//        })
        //图例：得到Lengend
        val legend = lineChart.getLegend()
        //隐藏Lengend
        legend.setEnabled(false);
        //隐藏描述
        val description = Description();
        description.setEnabled(false);
        lineChart.setDescription(description);
        //折线图点的标记
//        MyMarkerView mv = new MyMarkerView(this);
//        lineChart.setMarker(mv);
        //设置数据
        lineChart.setData(data);
        //图标刷新
        lineChart.invalidate();
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



