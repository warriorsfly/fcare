package com.wxsoft.fcare.ui.rating

import androidx.lifecycle.Observer
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.ActivityRatingSubjectBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_rating_subject_title.*

import javax.inject.Inject

class RatingSubjectActivity : BaseActivity() {

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val SCENCE_TYPE = "SCENCE_TYPE"
        const val RATING_ID = "RATING_ID"
        const val RATING_NAME = "RATING_NAME"
        const val RECORD_ID = "RECORD_ID"
    }

    @Inject
    lateinit var factory: ViewModelFactory
    private val toast: Toast by  lazy {
        Toast.makeText(this,"", Toast.LENGTH_SHORT)
    }
    private lateinit var adapter: SubjectAdapter
    private lateinit var viewModel: RatingSubjectViewModel

    private val patientId: String by lazyFast {
        intent?.getStringExtra(PATIENT_ID)?:""
    }

    private val ratingId: String by lazyFast {
        intent?.getStringExtra(RATING_ID)?:""
    }

    private val scenceType: String by lazyFast {
        intent?.getStringExtra(SCENCE_TYPE)?:""
    }

    private val ratingName: String by lazyFast {
        intent?.getStringExtra(RATING_NAME)?:""
    }

    private val recordId: String by lazyFast {
        intent?.getStringExtra(RECORD_ID)?:""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        val binding = DataBindingUtil.setContentView<ActivityRatingSubjectBinding>(
            this,
            R.layout.activity_rating_subject
        ).apply{

            viewModel=this@RatingSubjectActivity.viewModel

            lifecycleOwner = this@RatingSubjectActivity
        }

        page_title.text=ratingName
        viewModel.patientId=patientId
        viewModel.scenceType=scenceType

        adapter=SubjectAdapter(this)

        binding.list.adapter=adapter

        viewModel.rating.observe(this, Observer {
            it ?: return@Observer
            adapter.rating=it
            adapter.subjects=it.subjects
        })

        viewModel.mesAction.observe(this,EventObserver{
            toast.setText(it)
            toast.show()

            if(it=="保存成功") {
                Intent().let { intent ->
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        })
        back.setOnClickListener{onBackPressed()}
        viewModel.patientId=patientId
        viewModel.ratingId=ratingId
        viewModel.recordId=recordId


    }


}
