package com.wxsoft.fcare.ui.message

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityMessageBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class MessageActivity : BaseActivity()  {

    private lateinit var title:String
    private lateinit var content:String
    private lateinit var extra:String
    companion object {
        const val TITLE = "TITLE"
        const val CONTENT = "CONTENT"
        const val EXTRA = "EXTRA"
    }

    private lateinit var viewModel: MessageViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMessageBinding>(this, R.layout.activity_message)
            .apply {
                lifecycleOwner = this@MessageActivity
            }
        viewModel = viewModelProvider(factory)
        title=intent.getStringExtra(MessageActivity.TITLE)?:""
        content=intent.getStringExtra(MessageActivity.CONTENT)?:""
        extra=intent.getStringExtra(MessageActivity.EXTRA)?:""
        viewModel.extra = extra
        binding.viewModel = viewModel
        binding.title.setText(title)
        binding.title1.setText(content)

        viewModel.message.observe(this, Observer {
            onBackPressed()
        })

        viewModel.patient.observe(this, Observer {  })

    }

}
