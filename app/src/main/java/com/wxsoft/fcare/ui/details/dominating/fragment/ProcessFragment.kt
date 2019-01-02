package com.wxsoft.fcare.ui.details.dominating.fragment


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.BR

import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.FragmentProcessBinding
import com.wxsoft.fcare.ui.details.dominating.DoMinaViewModel
import com.wxsoft.fcare.utils.activityViewModelProvider
import com.wxsoft.fcare.utils.lazyFast
import dagger.android.support.DaggerFragment
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProcessFragment : DaggerFragment() {

    companion object {

        private const val ARG_STATUS = "arg.STATUS"
        fun newInstance( position:Int):ProcessFragment{

            val args = Bundle().apply {
                putInt(ARG_STATUS, position+1)
            }
            return ProcessFragment().apply { arguments = args }

        }
    }

    private lateinit var viewModel: DoMinaViewModel

    @Inject
    lateinit var factory: ViewModelFactory


    lateinit var binding: FragmentProcessBinding

    private val state: Int by lazyFast {
        val args = arguments ?: throw IllegalStateException("Missing arguments!")
        args.getInt(ARG_STATUS)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentProcessBinding.inflate(inflater,container, false).apply {
            setLifecycleOwner(this@ProcessFragment)
        }

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = activityViewModelProvider(factory)
        viewModel.task.observe(this, Observer {
            it ?: return@Observer
            binding.goTo.text =
                    when (state) {
                        1 -> it.arrivalTime?.toString()
                        2 -> it.arrivalTime?.toString()
                        3 -> it.editTime?.toString()
                        4 -> it.returningTime?.toString()
                        5 -> it.arriveHosTime?.toString()

                        else -> throw IllegalArgumentException("The state for task is invalid. Received: $state")
                    }

            binding.image.setImageResource(when(it.status){
                1-> R.drawable.ic_car_started
                else->R.drawable.ic_car_arrived
            })
        })

    }
}
