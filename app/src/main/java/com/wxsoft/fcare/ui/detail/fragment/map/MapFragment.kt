package com.wxsoft.fcare.ui.detail.fragment.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.MyLocationConfiguration
import com.baidu.mapapi.map.MyLocationData
import com.wxsoft.fcare.databinding.FragmentEmrMapBinding
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_emr_map.*
import com.wxsoft.fcare.R
import com.wxsoft.fcare.data.entity.GpsLocation
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.ui.detail.PatientDetailViewModel
import com.wxsoft.fcare.utils.activityViewModelProvider
import kotlinx.android.synthetic.main.activity_patient_detail.*
import javax.inject.Inject


class MapFragment : DaggerFragment() {


//    @Inject lateinit var factory: ViewModelFactory
//
    private lateinit var binding: FragmentEmrMapBinding
//
////    private var baiduMap:BaiduMap?=null
//
//    private lateinit var viewModel:PatientDetailViewModel
//    @Inject
//    lateinit var locationClient: LocationClient

//    private lateinit var locationListener:BDAbstractLocationListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentEmrMapBinding.inflate(inflater, container, false).
            apply {
                setLifecycleOwner (this@MapFragment)
            }
        return binding.root
    }

}

