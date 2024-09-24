package com.example.daejangjung2.feature.main.home.notice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingActivity
import com.example.daejangjung2.databinding.ActivityNoticeBinding
import com.example.daejangjung2.domain.model.Notice

class NoticeActivity : BindingActivity<ActivityNoticeBinding>(R.layout.activity_notice) {
    private val viewModel: NoticeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupObserve()
        setNotice()
    }

    private fun setupObserve(){
        viewModel.notice.observe(this){ uiUpdate(it) }
    }

    private fun uiUpdate(notice: Notice){
        binding.noticeTitle.text = notice.comment
    }

    private fun setNotice(){
        try{
            val notice: Notice? = intent.getParcelableExtra(NOTICE)
            if(notice!=null){
                viewModel.getNotice(notice)
            }
        }catch (e: Exception){
            Log.d(NOTICE_TAG, e.message.toString())
        }
    }

    companion object{
        private const val NOTICE = "notice"
        private const val NOTICE_TAG = "notice_tag"
        fun getIntent(context: Context, notice: Notice): Intent{
            return Intent(context, NoticeActivity::class.java).apply {
                putExtra(NOTICE, notice)
            }
        }
    }
}