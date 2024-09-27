package com.example.daejangjung2.feature.main

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingActivity
import com.example.daejangjung2.common.view.BackKeyHandler
import com.example.daejangjung2.databinding.ActivityMainBinding
import com.example.daejangjung2.feature.main.category.CategoryBottomSheetFragment
import com.example.daejangjung2.feature.main.community.CommunityFragment
import com.example.daejangjung2.feature.main.home.HomeFragment
import com.example.daejangjung2.feature.main.map.MapFragment
import com.example.daejangjung2.feature.main.mypage.MyPageFragment
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity: BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    private val backKeyHandler = BackKeyHandler(this)

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (viewModel.curScreen.value != ScreenType.ChangeScreenType.Home) { // 홈으로 이동시켜서 한 번 더 상품들을 노출시키기
                binding.bnvMain.selectedItemId = R.id.menu_item_home
                return
            }
            backKeyHandler.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        onBackPressedDispatcher.addCallback(this, callback)

        setupObserve()
        setSystemUiVisibility()
    }

    override fun onResume() {
        super.onResume()
        // 액티비티가 다시 화면에 보일 때도 설정 적용
        setSystemUiVisibility()
    }

    private fun setupObserve() {
        viewModel.curScreen.observe(this) { changeFragment(it) }

        viewModel.curPopScreenEvent.observe(this) {
            when (it) {
                ScreenType.PopScreenType.Category -> {
                    CategoryBottomSheetFragment.newInstance().show(supportFragmentManager, it.tag)
                }
            }
        }
    }

    private fun changeFragment(screenType: ScreenType.ChangeScreenType) {
        val fragment = supportFragmentManager.findFragmentByTag(screenType.tag)
        supportFragmentManager.commit {
            supportFragmentManager.fragments.forEach { existingFragment ->
                if (existingFragment.isVisible) {
                    detach(existingFragment)  // Fragment를 detach하여 완전히 lifecycle에서 뷰를 분리
                }
            }

            if (fragment == null) {
                // 새로운 Fragment를 추가
                add(
                    binding.fcvMain.id,
                    when (screenType) {
                        ScreenType.ChangeScreenType.Home -> HomeFragment.newInstance()
                        ScreenType.ChangeScreenType.Map -> MapFragment.newInstance()
                        ScreenType.ChangeScreenType.Community -> CommunityFragment.newInstance()
                        ScreenType.ChangeScreenType.MyPage -> MyPageFragment.newInstance()
                    },
                    screenType.tag
                )
            } else {
                attach(fragment)  // 기존 Fragment를 attach하여 다시 보여줌
            }
            setReorderingAllowed(true)  // 전환 애니메이션 등 최적화
        }
    }

    private fun setSystemUiVisibility() {
        // 상태 바 색상 강제 설정
        window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)

        // 상태 바 텍스트 색상 설정 (밝게)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}