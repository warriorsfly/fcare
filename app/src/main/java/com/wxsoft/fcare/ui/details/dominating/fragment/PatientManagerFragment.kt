package com.wxsoft.fcare.ui.details.dominating.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.ui.details.dominating.DoMinaViewModel
import com.wxsoft.fcare.ui.details.dominating.fragment.emr.EmrFragment
import com.wxsoft.fcare.utils.activityViewModelProvider
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_patient_manager.*
import javax.inject.Inject


class PatientManagerFragment : DaggerFragment() {


    @Inject lateinit var factory: ViewModelFactory

    lateinit var viewModel:DoMinaViewModel
    lateinit var adapter: EmrAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_patient_manager, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tab.setupWithViewPager(patPager)
        viewModel=activityViewModelProvider(factory)

        viewModel.task.observe(this, Observer {
            it ?: return@Observer

            if(patPager.adapter==null) {
                adapter = EmrAdapter(childFragmentManager, it.patients)

                patPager.adapter=adapter
            }


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
