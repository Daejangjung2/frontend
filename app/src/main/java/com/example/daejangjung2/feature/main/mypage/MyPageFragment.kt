package com.example.daejangjung2.feature.main.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingFragment
import com.example.daejangjung2.common.view.Toaster
import com.example.daejangjung2.databinding.FragmentMyPageBinding
import com.example.daejangjung2.feature.auth.login.LoginActivity

class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels { MyPageViewModel.Factory };

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        setupObserve()
    }

    private fun setupObserve() {
        viewModel.event.observe(viewLifecycleOwner){handleEvent(it)}
    }

    private fun handleEvent(event: MyPageViewModel.Event){
        when(event){
            MyPageViewModel.Event.Success -> {

            }
            is MyPageViewModel.Event.Failed -> {
                Toaster.showShort(requireContext(),event.message)
            }
            is MyPageViewModel.Event.Logout -> {
                startActivity(LoginActivity.getIntent(requireContext()))
                Toaster.showShort(requireContext(),event.message)
                requireActivity().finish()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE: Int = 1

        @JvmStatic
        fun newInstance() = MyPageFragment()
    }
}