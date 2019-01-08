package com.wxsoft.fcare.ui.rating

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityRatingSubjectBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.utils.lazyFast
import com.wxsoft.fcare.utils.viewModelProvider

import javax.inject.Inject

class RatingSubjectActivity : BaseActivity() {

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val RATING_ID = "RATING_ID"
    }

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var adapter: SubjectAdapter
    private lateinit var viewModel: RatingSubjectViewModel

    private val patientId: String by lazyFast {
        intent?.getStringExtra(PATIENT_ID)?:""
    }

    private val ratingId: String by lazyFast {
        intent?.getStringExtra(RATING_ID)?:""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        var binding = DataBindingUtil.setContentView<ActivityRatingSubjectBinding>(
            this,
            R.layout.activity_rating_subject
        ).apply{

            viewModel=this@RatingSubjectActivity.viewModel

            setLifecycleOwner(this@RatingSubjectActivity)
        }
        viewModel.patientId=patientId

        adapter=SubjectAdapter(this)

        binding.list.adapter=adapter

        viewModel.rating.observe(this, Observer {
            it ?: return@Observer
            adapter.rating=it
            adapter.subjects=it.subjects
        })

        viewModel.patientId=patientId
        viewModel.ratingId=ratingId

    }

}
