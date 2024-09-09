package com.example.daejangjung2.feature.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingActivity
import com.example.daejangjung2.databinding.ActivitySplashBinding
import com.example.daejangjung2.feature.auth.login.LoginActivity
import com.example.daejangjung2.feature.main.MainActivity

class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val viewModel: SplashViewModel by viewModels { SplashViewModel.Factory }
    private var isSplashOut = false
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { isSplashOut }
        setupObserve()
    }

    private fun setupObserve(){
        viewModel.event.observe(this){ handleEvent(it) }
    }

    private fun handleEvent(event:SplashViewModel.Event){
        when(event){
            SplashViewModel.Event.NavigateToLogin -> {
                startActivity(LoginActivity.getIntent(this@SplashActivity, true))
                finish()
            }
            SplashViewModel.Event.NavigateToMain ->{
                startActivity(MainActivity.getIntent(this@SplashActivity))
                finish()
            }
        }
    }
}