package com.wxsoft.fcare.ui.rating

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityRatingBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.EventAction
import com.wxsoft.fcare.ui.details.dominating.fragment.emr.EmrFragment
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider

import kotlinx.android.synthetic.main.layout_common_title.*
import java.lang.ref.WeakReference
import javax.inject.Inject

class RatingActivity : BaseActivity() {

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var adapter: RatingAdapter
    private lateinit var viewModel: RatingViewModel

    private val patientId: String by lazyFast {
        intent?.getStringExtra(PATIENT_ID)?:""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        var binding = DataBindingUtil.setContentView<ActivityRatingBinding>(
            this,
            R.layout.activity_rating
        ).apply{

            viewModel=this@RatingActivity.viewModel

            lifecycleOwner = this@RatingActivity
        }
        viewModel.patientId=patientId

        adapter=RatingAdapter(this)
        adapter.setActionListener(EventActions(WeakReference(this), patientId))
        binding.list.adapter=adapter
        binding.list.adapter=adapter

        viewModel.ratings.observe(this, Observer {
            it ?: return@Observer
            adapter.submitList(it)
        })

        back.setOnClickListener{onBackPressed()}

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==RESULT_OK) {
            when (requestCode) {
                EmrFragment.ARG_NEW_ITEM_CODE -> {

                    Intent().let { intent->
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        }
    }


    class EventActions constructor(private val context: WeakReference<FragmentActivity>, private val patientId:String):
        EventAction<Rating> {
        override fun onOpen(t: Rating) {
            var intent = Intent(context.get(), RatingSubjectActivity::class.java).apply {
                putExtra(RatingSubjectActivity.PATIENT_ID, patientId)
                putExtra(RatingSubjectActivity.RATING_ID, t.id)
                putExtra(RatingSubjectActivity.RATING_NAME, t.name)
            }
            context.get()?.startActivityForResult(intent,EmrFragment.ARG_NEW_ITEM_CODE)
        }

    }

}
