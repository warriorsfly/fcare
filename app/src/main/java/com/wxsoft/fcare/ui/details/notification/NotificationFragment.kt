package com.wxsoft.fcare.ui.details.notification

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.databinding.FragmentNotifyBinding

class NotificationFragment : Fragment() {

    companion object {
        fun newInstance() = NotificationFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentNotifyBinding.inflate(inflater, container, false).apply {

        }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}
