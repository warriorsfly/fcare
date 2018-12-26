package com.wxsoft.fcare.ui.detail.fragment.timeline

import android.Manifest
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.data.dictionary.ActionRes
import com.wxsoft.fcare.data.entity.EmrLog
import com.wxsoft.fcare.databinding.FragmentTimeLineBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.result.EventObserver
import com.wxsoft.fcare.service.JPushReceiver.Companion.LineRefresh
import com.wxsoft.fcare.ui.detail.PatientDetailViewModel
import com.wxsoft.fcare.ui.detail.dialog.Check.CheckDialog
import com.wxsoft.fcare.ui.detail.dialog.VitalSignDialog
import com.wxsoft.fcare.ui.detail.dialog.heart.HeartDialog
import com.wxsoft.fcare.ui.detail.dialog.inital.DiagnosisDialog
import com.wxsoft.fcare.ui.detail.dialog.notify.NotifycationDialog
import com.wxsoft.fcare.ui.detail.dialog.share.ShareDialog
import com.wxsoft.fcare.ui.detail.dialog.transfer.TransferDialog
import com.wxsoft.fcare.ui.detail.fragment.conduitroom.ActivateConduitRoomFragment
import com.wxsoft.fcare.ui.detail.fragment.conduitroom.ArriveConduitFragment
import com.wxsoft.fcare.ui.detail.fragment.conduitroom.StartConduitRoomFragment
import com.wxsoft.fcare.ui.detail.fragment.detour.DetourFragment
import com.wxsoft.fcare.ui.detail.fragment.diagnosis.DischargeDiagnosisFragment
import com.wxsoft.fcare.ui.detail.fragment.drug.DrugFragment
import com.wxsoft.fcare.ui.detail.fragment.evaluation.EvaluationFragment
import com.wxsoft.fcare.ui.detail.fragment.grace.GraceFragment
import com.wxsoft.fcare.ui.detail.fragment.outcomes.OutcomesFragment
import com.wxsoft.fcare.ui.detail.fragment.pci.PCIFragment
import com.wxsoft.fcare.ui.detail.fragment.profile.ProfileFragment
import com.wxsoft.fcare.ui.detail.fragment.thrombolysis.PreThrombolysisFragment
import com.wxsoft.fcare.ui.detail.fragment.thrombolysis.WithinThrombolysisFragment
import com.wxsoft.fcare.utils.activityViewModelProvider
import com.wxsoft.fcare.utils.clearDecorations
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.layout_operation_menu_top.*
import kotlinx.android.synthetic.main.layout_operation_menus.*
import javax.inject.Inject


class TimeLineFragment : DaggerFragment() {

    companion object {
        const val CAMERA_PIC_REQUEST=10
        const val CAMERA_PERMISSION_REQUEST=11

    }

    private lateinit var viewModel: PatientDetailViewModel

    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<*>

    private lateinit var adapter: OpertionMenuAdapter

    private var receiver: TimeLineBoardCastReceiver? =null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var binding: FragmentTimeLineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activityViewModelProvider(viewModelFactory)
        receiver= TimeLineBoardCastReceiver(viewModel)
        val intentFilter = IntentFilter()
        // 2. 设置接收广播的类型
        intentFilter.addAction(LineRefresh)
        activity?.registerReceiver(receiver, intentFilter)


        viewModel.navigateToOperationAction.observe(this, EventObserver {

            menuBottomSheetBehavior.state=BottomSheetBehavior.STATE_HIDDEN
            when(it){
                ActionRes.ActionType.病情评估->{

                    var dialog=EvaluationFragment()
                    dialog.show(childFragmentManager,EvaluationFragment.TAG)
                }
                ActionRes.ActionType.生命体征->{
                    var dialog= VitalSignDialog()
                    dialog.show(childFragmentManager, VitalSignDialog.DIALOG_VITAL_SIGNS)
                }
                ActionRes.ActionType.消息通知->{
                    var dialog= NotifycationDialog()
                    dialog.patientId=viewModel.patientId
                    dialog.show(childFragmentManager,NotifycationDialog.TAG)
                }

                ActionRes.ActionType.心电图->{
                    checkCameraPermission()

                }

                ActionRes.ActionType.辅助检查->{
                    viewModel.getAssistCheck()
                    var dialog= CheckDialog()
                    dialog.patientId=viewModel.patientId
                    dialog.show(childFragmentManager, CheckDialog.TAG)
                }

                ActionRes.ActionType.给药->{
                    viewModel.loadDrugDetail()
                    var dialog= DrugFragment()
                    dialog.show(childFragmentManager,DrugFragment.DIALOG_DRUG)
                }

                ActionRes.ActionType.溶栓处置->{
                    thrombolyticTreatment()
                }
                ActionRes.ActionType.GRACE->{
                    viewModel.getgrace()

                    var dialog= GraceFragment()
                    dialog.show(childFragmentManager,GraceFragment.DIALOG_GRACE)
                }
                ActionRes.ActionType.绕行导管室->{
                    viewModel.getDetour()
                    var dialog= DetourFragment()
                    dialog.show(childFragmentManager,DetourFragment.DIALOG_DETOUR)
                }

                ActionRes.ActionType.启动导管室->{//启动导管室
                    viewModel.loadPci()
                    var dialog= StartConduitRoomFragment()
                    dialog.show(childFragmentManager,StartConduitRoomFragment.DIALOG_START_CONDUIT_ROOM)
                }

                ActionRes.ActionType.激活导管室->{//激活导管室
                    viewModel.loadPci()
                    var dialog= ActivateConduitRoomFragment()
                    dialog.show(childFragmentManager,ActivateConduitRoomFragment.DIALOG_ACTIVE_CONDUIT_ROOM)
                }

                ActionRes.ActionType.到达导管室->{//到达导管室
                    viewModel.loadPci()
                    var dialog= ArriveConduitFragment()
                    dialog.show(childFragmentManager,ArriveConduitFragment.DIALOG_ARRIVE_CONDUIT_ROOM)
                }

                ActionRes.ActionType.出院诊断->{
                    viewModel.getOutHospitalDiagnosis()
                    var dialog= DischargeDiagnosisFragment()
                    dialog.show(childFragmentManager,DischargeDiagnosisFragment.DIALOG_DISCHARGE_DIAGNOSIS)
                }

                ActionRes.ActionType.患者转归->{
                    viewModel.getOutCome()
                    var dialog= OutcomesFragment()
                    dialog.show(childFragmentManager,OutcomesFragment.DIALOG_OUTCOMES)
                }
                ActionRes.ActionType.PCI->{
                    viewModel.loadPci()
                    var dialog= PCIFragment()
                    dialog.show(childFragmentManager,PCIFragment.DIALOG_PCI)
                }

                ActionRes.ActionType.来院方式->{
                    var dialog=TransferDialog()
                    dialog.show(childFragmentManager,TransferDialog.TAG)
                    dialog.patientId= viewModel.patientId
                }

                ActionRes.ActionType.初步诊断->{
                    var dialog= DiagnosisDialog()
                    dialog.patientId=viewModel.patientId
                    dialog.show(childFragmentManager,DiagnosisDialog.TAG)
                }
            }
        })


        adapter = OpertionMenuAdapter(this,viewModel)

        viewModel.menuItems.observe(this@TimeLineFragment, Observer { t->

            adapter.menus=t?: emptyList()
            adapter.notifyDataSetChanged()
//            t?.let {
//                adapter.menus.clear()
//                adapter.menus.addAll(t)
//                adapter.notifyDataSetChanged()
//                menuBottomSheetBehavior.statu = BottomSheetBehavior.STATE_HIDDEN
//            }
        })

        if(viewModel.patientId.isEmpty()){
            var dialog= ProfileFragment()

            dialog.show(childFragmentManager,ProfileFragment.DIALOG_PROFILE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTimeLineBinding.inflate(inflater, container, false)
            .apply {
                setLifecycleOwner(this@TimeLineFragment)
            }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        receiver?.let {
            activity?.unregisterReceiver(receiver)
            receiver?.vm=null
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewModel=viewModel
        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottom.root)
        menuBottomSheetBehavior.setBottomSheetCallback(object:BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(p0: View, p1: Float) {}

            override fun onStateChanged(p0: View, p1: Int) {
                if(p1==BottomSheetBehavior.STATE_HIDDEN){
                    viewModel.menuShown(false)
                }else if(p1==BottomSheetBehavior.STATE_EXPANDED){
                    viewModel.menuShown(true)
                }
            }

        })
        menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        show_menus.setOnClickListener {
            if (menuBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

        }

        edit_profile.setOnClickListener{

            var dialog= ProfileFragment()
            dialog.show(childFragmentManager,ProfileFragment.DIALOG_PROFILE)
        }

        transfer.setOnClickListener{
            var dialog=TransferDialog()
            dialog.show(childFragmentManager,TransferDialog.TAG)
            dialog.patientId= viewModel.patientId
//            var dialog= ProfileFragment()
//            dialog.show(childFragmentManager,ProfileFragment.DIALOG_PROFILE)
        }

        share.setOnClickListener{

            val dialog=ShareDialog()
            dialog.patientId=viewModel.patientId
            dialog.show(childFragmentManager,DiagnosisDialog.TAG)
        }

        menuInclude.adapter = adapter
    }

    //溶栓处置点击
    private fun thrombolyticTreatment(){
        val list: Array<String> = arrayOf("院前溶栓治疗","院内溶栓治疗")
        AlertDialog.Builder(this.context).setItems(list) { _, i ->
            when(i) {
                0 -> {//院前溶栓治疗
                    viewModel.getThrom(true)
                    var dialog= PreThrombolysisFragment()
                    dialog.show(childFragmentManager,PreThrombolysisFragment.DIALOG_PRE_THROMBOLYSIS)
                }
                1 -> {//院内溶栓治疗
                    viewModel.getThrom(false)
                    var dialog= WithinThrombolysisFragment()
                    dialog.show(childFragmentManager,WithinThrombolysisFragment.DIALOG_WITHIN_THROMBOLYSIS)
                }
            }
        }.create().show()
    }

    class TimeLineBoardCastReceiver(var vm: PatientDetailViewModel?): BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val pid = intent?.getStringExtra("pid")
            if (pid!=null && pid == vm?.patientId) {
                vm?.onSwipeRefresh()
            }
        }
    }


    private fun checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(activity!!,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(activity!!,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {


            requestPermissions(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                CAMERA_PERMISSION_REQUEST
            )

        }else{
            showPhotoTaking()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_PERMISSION_REQUEST->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    showPhotoTaking()
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (CAMERA_PIC_REQUEST == requestCode) {
            val photo = data?.extras?.get("data") as Bitmap

            var dialog = HeartDialog()
            dialog.patientId = viewModel.patientId
            dialog.bitmap=photo
            dialog.show(childFragmentManager, HeartDialog.TAG)
        }
    }

    private fun showPhotoTaking(){
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST)
    }

}

@BindingAdapter(value = ["timelineItems"])
fun timeLineItems(recyclerView: RecyclerView, list: List<EmrLog>?) {
    if (recyclerView.adapter == null) {
        recyclerView.adapter = TimeAdapter()
    }
    (recyclerView.adapter as TimeAdapter).apply {
        this.submitList(list ?: emptyList())
    }

    recyclerView.clearDecorations()
    if (list != null && list.isNotEmpty()) {
        recyclerView.addItemDecoration(
            ItemDecoration(recyclerView.context, list)
        )

    }
}
//TODO top菜单悬浮在bottom菜单的上方