package com.example.daejangjung2.feature.main.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingFragment
import com.example.daejangjung2.databinding.FragmentMapBinding
import com.example.daejangjung2.feature.main.mypage.MyPageFragment

class MapFragment : BindingFragment<FragmentMapBinding>(R.layout.fragment_map) {
    private val viewModel: MapViewModel by viewModels()

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