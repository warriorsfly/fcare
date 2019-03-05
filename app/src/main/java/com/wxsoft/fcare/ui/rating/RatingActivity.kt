package com.wxsoft.fcare.ui.rating

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.data.entity.rating.RatingResult
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityRatingBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.dominating.fragment.emr.EmrFragment
import kotlinx.android.synthetic.main.activity_rating.*
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject


class RatingActivity : BaseActivity() {

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var adapter: RatingResultAdapter
    private lateinit var viewModel: RatingViewModel

    private val ratingFragment by lazy{
        RatingsSheetFragment(::newRating)
    }
    private val patientId: String by lazyFast {
        intent?.getStringExtra(PATIENT_ID)?:""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        adapter=RatingResultAdapter(this,::newItem,::showDetail)
        DataBindingUtil.setContentView<ActivityRatingBinding>(
            this,
            R.layout.activity_rating
        ).apply{
            list.adapter=this@RatingActivity.adapter

            viewModel=this@RatingActivity.viewModel

            lifecycleOwner = this@RatingActivity
        }
        viewModel.patientId=patientId

        viewModel.scenceRatings.observe(this, Observer {
            adapter.items=it
        })
        back.setOnClickListener{onBackPressed()}
        list.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL).apply {
            setDrawable(resources.getDrawable(R.drawable.divider_scencerating,theme))
        })
    }

    private fun newItem(scence:String) {
        viewModel.scenceId=scence
        ratingFragment.show(supportFragmentManager,RatingsSheetFragment.TAG)

    }

    private fun showDetail(result:RatingResult){}
    private fun newRating(rating:Rating){

        val intent = Intent(this, RatingSubjectActivity::class.java).apply {
            putExtra(RatingSubjectActivity.PATIENT_ID, patientId)
            putExtra(RatingSubjectActivity.SCENCE_TYPE, viewModel.scenceId)
            putExtra(RatingSubjectActivity.RATING_NAME, rating.name)
            putExtra(RatingSubjectActivity.RATING_ID, rating.id)
        }
        startActivityForResult(intent, EmrFragment.ARG_NEW_ITEM_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==RESULT_OK) {
            when (requestCode) {
                EmrFragment.ARG_NEW_ITEM_CODE -> {

                    Intent().let { intent->
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }
    }

//
//    class EventActions constructor(private val context: WeakReference<androidx.fragment.app.FragmentActivity>, private val patientId:String):
//        EventAction<Rating> {
//        override fun onOpen(t: Rating) {
//            val intent = Intent(context.get(), RatingSubjectActivity::class.java).apply {
//                putExtra(RatingSubjectActivity.PATIENT_ID, patientId)
//                putExtra(RatingSubjectActivity.RATING_ID, t.id)
//                putExtra(RatingSubjectActivity.RATING_NAME, t.name)
//            }
//            context.get()?.startActivityForResult(intent,EmrFragment.ARG_NEW_ITEM_CODE)
//        }
//
//    }

}
