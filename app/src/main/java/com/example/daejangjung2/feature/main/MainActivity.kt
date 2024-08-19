package com.example.daejangjung2.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
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

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
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
            supportFragmentManager.fragments.forEach(::hide)
            if (fragment == null) {
                add(
                    binding.fcvMain.id,
                    when (screenType) {
                        ScreenType.ChangeScreenType.Home -> HomeFragment.newInstance()
                        ScreenType.ChangeScreenType.Map -> MapFragment.newInstance()
                        ScreenType.ChangeScreenType.Community -> CommunityFragment.newInstance()
                        ScreenType.ChangeScreenType.MyPage -> MyPageFragment.newInstance()
                    },
                    screenType.tag,
                )
            } else {
                show(fragment)
            }
            setReorderingAllowed(true) // 전환 애니메이션 등 최적화
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}