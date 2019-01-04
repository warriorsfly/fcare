package com.wxsoft.fcare.ui.details.dominating.fragment

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.FragmentPatientManagerBinding
import com.wxsoft.fcare.ui.details.dominating.DoMinaViewModel
import com.wxsoft.fcare.ui.details.dominating.fragment.emr.EmrFragment
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.utils.activityViewModelProvider
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_patient_manager.*
import javax.inject.Inject


class PatientManagerFragment : DaggerFragment() {


    @Inject lateinit var factory: ViewModelFactory

    lateinit var viewModel:DoMinaViewModel
    lateinit var adapter: EmrAdapter
    lateinit var binding: FragmentPatientManagerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentPatientManagerBinding.inflate(inflater,container,false).apply {
            setLifecycleOwner(this@PatientManagerFragment)

        }


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tab.setupWithViewPager(patPager)

        viewModel=activityViewModelProvider(factory)

        binding.viewModel=viewModel
        if(!binding.image.hasOnClickListeners()) {
            binding.image.setOnClickListener {
                var intent = Intent(activity, ProfileActivity::class.java).apply {
                    putExtra(ProfileActivity.TASK_ID, viewModel.taskId)
                }
                startActivity(intent)

            }
        }
        viewModel.task.observe(this, Observer {
            it ?: return@Observer

            if(patPager.adapter==null) {
                adapter = EmrAdapter(childFragmentManager, it.patients)

                patPager.adapter=adapter
            }

//            if(it.patients.size==0){
//                patPager.visibility=View.GONE
//            }
        })



    }


    inner class EmrAdapter(fm: FragmentManager,patients:Array<Patient>) :
        FragmentPagerAdapter(fm) {

        private var fragments:MutableList<Fragment> = patients.map { EmrFragment.newInstance(it.id) }.toMutableList()
        private var titles:MutableList<String> = patients.map { it.name }.toMutableList()

        fun add(patient:Patient){

            fragments.add( EmrFragment.newInstance(patient.id) )
            titles.add(patient.name)
        }


        override fun getItem(position: Int): Fragment {

            return fragments[position]
        }

        override fun getCount(): Int {
            return titles.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
    companion object {

        @JvmStatic
        fun newInstance() =
            PatientManagerFragment()
    }
}
