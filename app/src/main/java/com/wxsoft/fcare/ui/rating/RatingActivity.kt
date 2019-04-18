package com.wxsoft.fcare.ui.rating

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.data.entity.rating.RatingResult
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityRatingBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.utils.ActionCode.Companion.ARG_NEW_ITEM_CODE
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject
import javax.inject.Named


class RatingActivity : BaseActivity() {

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var adapter: RatingAdapter
    private lateinit var viewModel: RatingViewModel

    private val ratingFragment by lazy{
        RatingsSheetFragment(::newRating)
    }
    private val patientId: String by lazyFast {
        intent?.getStringExtra(PATIENT_ID)?:""
    }

    @Inject
    @field:Named("ratingResultViewPool")
    lateinit var emrItemViewPool: RecyclerView.RecycledViewPool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        adapter=RatingAdapter(this,::showDetail)
        DataBindingUtil.setContentView<ActivityRatingBinding>(
            this,
            R.layout.activity_rating
        ).apply{
            list.adapter=this@RatingActivity.adapter
            list.addItemDecoration(DividerItemDecoration(this@RatingActivity,DividerItemDecoration.VERTICAL))
            viewModel=this@RatingActivity.viewModel

            lifecycleOwner = this@RatingActivity
        }
        viewModel.patientId=patientId

        viewModel.scenceRatings.observe(this, Observer {
            adapter.submitList(it)
        })

        setSupportActionBar(toolbar)
        title="评分"
    }

    private fun newItem(scence:String) {
        viewModel.scenceId=scence
        ratingFragment.show(supportFragmentManager,RatingsSheetFragment.TAG)

    }

    private fun showDetail(result:RatingResult){

        val intent = Intent(this, RatingSubjectActivity::class.java).apply {
            putExtra(RatingSubjectActivity.PATIENT_ID, patientId)
//            putExtra(RatingSubjectActivity.SCENCE_TYPE, viewModel.scenceId)
            putExtra(RatingSubjectActivity.RATING_NAME, result.ratingName)
//            putExtra(RatingSubjectActivity.RATING_ID, result.ratingId)
            putExtra(RatingSubjectActivity.RECORD_ID, result.id)
        }
        startActivityForResult(intent, ARG_NEW_ITEM_CODE)
    }
    private fun newRating(rating:Rating){
        ratingFragment.dismiss()
        val intent = Intent(this, RatingSubjectActivity::class.java).apply {
            putExtra(RatingSubjectActivity.PATIENT_ID, patientId)
            putExtra(RatingSubjectActivity.SCENCE_TYPE, viewModel.scenceId)
            putExtra(RatingSubjectActivity.RATING_NAME, rating.name)
            putExtra(RatingSubjectActivity.RATING_ID, rating.id)
        }
        startActivityForResult(intent, ARG_NEW_ITEM_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==RESULT_OK) {
            when (requestCode) {
                ARG_NEW_ITEM_CODE -> {

                    viewModel.refresh()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.new_item->{
                ratingFragment.show(supportFragmentManager,RatingsSheetFragment.TAG)
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}
