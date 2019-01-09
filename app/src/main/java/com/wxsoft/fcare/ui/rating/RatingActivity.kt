package com.wxsoft.fcare.ui.rating

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.data.dictionary.ActionRes
import com.wxsoft.fcare.databinding.ActivityRatingBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.EventAction
import com.wxsoft.fcare.ui.EventActions
import com.wxsoft.fcare.ui.common.AttachmentAdapter
import com.wxsoft.fcare.ui.details.dominating.fragment.emr.EmrFragment
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.patient.ProfileViewModel
import com.wxsoft.fcare.utils.lazyFast
import com.wxsoft.fcare.utils.viewModelProvider

import kotlinx.android.synthetic.main.activity_rating.*
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

            setLifecycleOwner(this@RatingActivity)
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


    class EventActions constructor(private val context: WeakReference<Context>, private val patientId:String):
        EventAction<Rating> {
        override fun onOpen(rating: Rating) {
            var intent = Intent(context.get(), RatingSubjectActivity::class.java).apply {
                putExtra(RatingSubjectActivity.PATIENT_ID, patientId)
                putExtra(RatingSubjectActivity.RATING_ID, rating.id)
                putExtra(RatingSubjectActivity.RATING_NAME, rating.name)
            }
            context.get()?.startActivity(intent)
        }

    }

}
