package com.wxsoft.fcare.ui.details.dominating.fragment


import android.os.Bundle
import android.os.PersistableBundle
import androidx.databinding.DataBindingUtil
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.MyLocationConfiguration
import com.baidu.mapapi.map.MyLocationData
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.ui.details.dominating.DoMinaViewModel
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.FragmentTaskGisBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.fragment_task_gis.*
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class GisActivity : BaseActivity() {

    private lateinit var viewModel: DoMinaViewModel

    @Inject
    lateinit var factory: ViewModelFactory


    lateinit var binding: FragmentTaskGisBinding

    private var baiduMap: BaiduMap?=null

    @Inject
    lateinit var locationClient: LocationClient

    private lateinit var locationListener: BDAbstractLocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<FragmentTaskGisBinding>(this, R.layout.fragment_task_gis)
            .apply {
                baiduMap = mapView.map
                baiduMap!!.setIndoorEnable(true)
                baiduMap!!.isMyLocationEnabled = true
                var marker = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_geo)
                var config = MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, marker)
                baiduMap!!.setMyLocationConfiguration(config)
                locationListener=object :BDAbstractLocationListener(){

                    override fun onReceiveLocation(location: BDLocation?) {

                        if(location!=null ) {
                            // 设置定位数据
                            val locData = MyLocationData.Builder()
                                .accuracy(location.radius)
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                                .direction(100f).latitude(location.latitude)
                                .longitude(location.longitude).build()

                            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
                            baiduMap!!.setMyLocationData(locData)

//                        viewModel.uploadGpsLocation(GpsLocation(viewModel.patientId,"测试车辆",location.longitude,location.latitude))

                        }
                    }
                }
                locationClient.registerLocationListener(locationListener)
                locationClient.start()
                lifecycleOwner = this@GisActivity
            }

        setSupportActionBar(toolbar)
        title="GPS信息"

    }
    override fun onDestroy() {

        super.onDestroy()
        locationClient?.stop()
        locationClient?.unRegisterLocationListener(locationListener)
        mapView?.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
//        if(baiduMap==null) {
////            baiduMap = mapView.map
////            baiduMap!!.setIndoorEnable(true)
////            baiduMap!!.isMyLocationEnabled = true
////            var marker = BitmapDescriptorFactory
////                .fromResource(R.drawable.icon_geo)
////            var config = MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, marker)
////            baiduMap!!.setMyLocationConfiguration(config)
////            locationListener=object :BDAbstractLocationListener(){
////
////                override fun onReceiveLocation(location: BDLocation?) {
////
////                    if(location!=null ) {
////                        // 设置定位数据
////                        val locData = MyLocationData.Builder()
////                            .accuracy(location.radius)
////                            // 此处设置开发者获取到的方向信息，顺时针0-360
////                            .direction(100f).latitude(location.latitude)
////                            .longitude(location.longitude).build()
////
////                        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
////                        baiduMap!!.setMyLocationData(locData)
////
//////                        viewModel.uploadGpsLocation(GpsLocation(viewModel.patientId,"测试车辆",location.longitude,location.latitude))
////
////                    }
////                }
////            }
////            locationClient.registerLocationListener(locationListener)
////            locationClient.start()
//        }
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

}
