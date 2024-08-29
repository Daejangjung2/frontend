package com.example.daejangjung2.feature.main.home

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingFragment
import com.example.daejangjung2.databinding.FragmentHomeBinding
import com.example.daejangjung2.domain.model.ImageBanner
import com.example.daejangjung2.feature.main.home.banner.BannerAdapter
import com.example.daejangjung2.feature.main.mypage.MyPageFragment

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels();
    private lateinit var banner: ViewPager2
    private lateinit var layout: LinearLayout;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        banner = view.findViewById(R.id.viewPager_banner)
        layout = view.findViewById(R.id.layoutIndicators)


        setupObserve()
        viewModel.getBanner()
    }

    private fun setupObserve(){
        viewModel.banner.observe(viewLifecycleOwner){ setBanner(it) }
    }

    private fun setBanner(banners: ArrayList<ImageBanner>){
        banner.currentItem=0
        banner.offscreenPageLimit=1
        banner.adapter = BannerAdapter(requireContext(), banners)

        var currentPage = 0
        val handler = Handler()

        val update = Runnable {
            if(currentPage == banners.size) currentPage=0
            banner.setCurrentItem(currentPage++, true)
        }

        val delay: Long = 3000
        handler.postDelayed(object : Runnable {
            override fun run() {
                update.run()
                handler.postDelayed(this, delay)
            }
        }, delay)

        banner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })

        setupIndicators(banners.size)
    }

    private fun setupIndicators(count: Int) {
        val indicators = arrayOfNulls<ImageView>(count)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(16, 8, 16, 8)

        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_indicator_inactive
                )
            )
            indicators[i]?.layoutParams = params
            layout.addView(indicators[i])
        }
        setCurrentIndicator(0)
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = layout.childCount
        for (i in 0 until childCount) {
            val imageView = layout.getChildAt(i) as? ImageView
            if (i == position) {
                imageView?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_indicator_active
                    )
                )
            } else {
                imageView?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_indicator_inactive
                    )
                )
            }
        }
    }

    companion object {
        private const val REQUEST_CODE: Int = 1
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}