package com.wxsoft.fcare.ui.detail.dialog.heart


import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.databinding.DialogHeartBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.widget.WxDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class HeartDialog : WxDimDialogFragment(), HasSupportFragmentInjector {

    @Inject
    lateinit var factory: ViewModelFactory


    private lateinit var binding:DialogHeartBinding

    var patientId:String=""

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DialogHeartBinding.inflate(inflater,container,false).apply {
            setLifecycleOwner(this@HeartDialog)
        }
        if(bitmap!=null)
        binding.image.setImageBitmap(bitmap)
        return binding.root
    }

    var bitmap:Bitmap?=null


    companion object {
        const val TAG = "heart_dialog"
    }

}


