package com.wxsoft.fcare.ui.rating

import android.app.Activity
import android.app.AlertDialog
import androidx.lifecycle.Observer
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.ActivityRatingSubjectBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_new_title.*

import javax.inject.Inject
import javax.inject.Named

class RatingSubjectActivity : BaseActivity() {

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val SCENCE_TYPE = "SCENCE_TYPE"
        const val RATING_ID = "RATING_ID"
        const val RATING_NAME = "RATING_NAME"
        const val RECORD_ID = "RECORD_ID"
    }

    @Inject
    @field:Named("ratingOptionViewPool")
    lateinit var optionViewPool: RecyclerView.RecycledViewPool


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
        viewModel.patientId=patientId
        viewModel.scenceType=scenceType

        adapter=SubjectAdapter(this,optionViewPool)

        binding.list.adapter=adapter

        viewModel.rating.observe(this, Observer {
            it ?: return@Observer
            adapter.setRat(it)
            adapter.submitList(it.subjects)
        })

        viewModel.mesAction.observe(this,EventObserver{
            toast.setText(it)
            toast.show()
        })

        viewModel.savingResult.observe(this, Observer {
            if(it){
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        })
        viewModel.patientId=patientId
        viewModel.ratingId=ratingId
        viewModel.recordId=recordId

        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_subject2,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{
                viewModel.saveRecord()
                true
            }
            R.id.delete->{

                if(recordId.isNotEmpty()) {
                    val dialog = AlertDialog.Builder(this,R.style.Theme_FCare_Dialog_Text)
                    dialog.setTitle("是否删除当前评分?")
                        .setPositiveButton("确定") { _, _ -> viewModel.deleteRecord() }
                        .setNegativeButton("取消") { _, _ -> }
                        .create().show()
                }
                true
            }
            else->super.onOptionsItemSelected(item)
        }

    }
}
