package com.example.daejangjung2.feature.main.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingFragment
import com.example.daejangjung2.databinding.FragmentCommunityBinding
import com.example.daejangjung2.feature.main.mypage.MyPageFragment

class CommunityFragment : BindingFragment<FragmentCommunityBinding>(R.layout.fragment_community){
    private val viewModel: CommunityViewModel by viewModels();

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        setupObserve()
    }

    private fun setupObserve(){

    }

    companion object {
        private const val REQUEST_CODE: Int = 1
        @JvmStatic
        fun newInstance() = MyPageFragment()
    }
}