package com.example.daejangjung2.feature.auth.signup.consent

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingActivity
import com.example.daejangjung2.databinding.ActivityConsentBinding
import com.example.daejangjung2.feature.auth.signup.ConsentType
import com.example.daejangjung2.feature.main.MainActivity

class ConsentActivity : BindingActivity<ActivityConsentBinding>(R.layout.activity_consent) {
    private val viewModel: ConsentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        try{
            viewModel.consentText()
        }catch (e: Exception){
            Log.e(ERROR, e.toString())
        }
    }



    companion object {
        private val CONSENT_TYPE = "CONSENT_TYPE"
        private val ERROR = "ERROR"

        fun getIntent(context: Context, type: String): Intent {
            return Intent(context, ConsentActivity::class.java).apply {
                putExtra(CONSENT_TYPE, type)
            }
        }
    }
}