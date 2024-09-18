package com.example.daejangjung2.feature.main.map.newsInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.daejangjung2.R
import com.example.daejangjung2.databinding.FragmentNewsBinding
import com.example.daejangjung2.domain.model.NewsInfo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewsFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentNewsBinding? = null;
    private val binding: FragmentNewsBinding
        get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()

    private lateinit var newsInfoAdapter: NewsInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setObserve()
        viewModel.getTestNews()
    }

    private fun setObserve(){
        viewModel.event.observe(viewLifecycleOwner){ handleEvent(it) }
        viewModel.news.observe(viewLifecycleOwner){ setNews(it) }
    }

    private fun handleEvent(event: NewsViewModel.Event){

    }

    private fun setNews(newsInfo: List<NewsInfo>){
        val news = binding.rvNewsInfo

        newsInfoAdapter = NewsInfoAdapter(requireContext(), newsInfo)
        newsInfoAdapter.notifyDataSetChanged()

        news.adapter = newsInfoAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}