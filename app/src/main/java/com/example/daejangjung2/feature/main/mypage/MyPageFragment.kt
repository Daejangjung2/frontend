package com.example.daejangjung2.feature.main.mypage

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingFragment
import com.example.daejangjung2.data.model.response.ProfileResponse
import com.example.daejangjung2.databinding.FragmentMyPageBinding

class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels{ MyPageViewModel.Factory };

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        setupObserve()
        viewModel.myProfile()
    }

    private fun setupObserve(){
        viewModel.profile.observe(viewLifecycleOwner){ setUiUpdate(it) }
    }

    private fun setUiUpdate(profile: ProfileResponse){
        binding.tvName.text = profile.nickname
        binding.tvEmailPhone.text = profile.email
        Glide.with(this@MyPageFragment)
            .load(profile.profileImg)
            .into(binding.profileImage)
    }

    companion object {
        private const val REQUEST_CODE: Int = 1
        @JvmStatic
        fun newInstance() = MyPageFragment()
    }
}