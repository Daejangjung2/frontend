package com.example.daejangjung2.feature.main.community

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingFragment
import com.example.daejangjung2.databinding.FragmentCommunityBinding
import com.example.daejangjung2.feature.main.community.post.WritePostFragment
import com.example.daejangjung2.feature.main.community.websocket.WebSocketManager
import com.example.daejangjung2.feature.main.map.MapFragment
import com.example.daejangjung2.feature.main.mypage.MyPageFragment

class CommunityFragment : BindingFragment<FragmentCommunityBinding>(R.layout.fragment_community){
    private val viewModel: CommunityViewModel by viewModels{CommunityViewModel.Factory};
    private lateinit var adapter: CommunityPostAdapter
    private lateinit var webSocketManager: WebSocketManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        viewModel.fetchAllPosts()  // 모든 게시물 가져오기

        // RecyclerView 설정
        adapter = CommunityPostAdapter()
        binding.communityRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.communityRecyclerView.adapter = adapter

        //웹소켓
        webSocketManager = WebSocketManager()
        webSocketManager.startWebSocket()  // WebSocket 시작

        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_all -> {
                    // All 선택 시 처리할 작업
                    setupButtonListeners(true, false, false, false, false, false, false, false, false, false, false, false, false)
                    viewModel.fetchAllPosts()  // 모든 게시물 가져오기
                }
                R.id.chip_ara -> {
                    // 아라자전거길 선택 시 처리할 작업
                    setupButtonListeners(false, true, false, false, false, false, false, false, false, false, false, false, false)
                    viewModel.fetchLocationPosts("아라 자전거길")
                }
                R.id.chip_hangang -> {
                    // 한강종주자전거길 선택 시 처리할 작업
                    setupButtonListeners(false, false, true, false, false, false, false, false, false, false, false, false, false)
                    viewModel.fetchLocationPosts("한강종주 자전거길")
                }
                R.id.chip_nam -> {
                    // 남한강자전거길 선택 시 처리할 작업
                    setupButtonListeners(false, false, false, true, false, false, false, false, false, false, false, false, false)
                    viewModel.fetchLocationPosts("남한강 자전거길")
                }
                R.id.chip_buk -> {
                    // 북한강자전거길 선택 시 처리할 작업
                    setupButtonListeners(false, false, false, false, true, false, false, false, false, false, false, false, false)
                    viewModel.fetchLocationPosts("북한강 자전거길")
                }
                R.id.chip_saje -> {
                    // 새재자전거길 선택 시 처리할 작업
                    setupButtonListeners(false, false, false, false, false, true, false, false, false, false, false, false, false)
                    viewModel.fetchLocationPosts("새재 자전거길")
                }
                R.id.chip_nak -> {
                    // 낙동강자전거길 선택 시 처리할 작업
                    setupButtonListeners(false, false, false, false, false, false, true, false, false, false, false, false, false)
                    viewModel.fetchLocationPosts("낙동강 자전거길")
                }
                R.id.chip_kum -> {
                    // 금강자전거길 선택 시 처리할 작업
                    setupButtonListeners(false, false, false, false, false, false, false, true, false, false, false, false, false)
                    viewModel.fetchLocationPosts("금강 자전거길")
                }
                R.id.chip_young -> {
                    // 영동강자전거길 선택 시 처리할 작업
                    setupButtonListeners(false, false, false, false, false, false, false, false, true, false, false, false, false)
                    viewModel.fetchLocationPosts("영산강 자전거길")
                }
                R.id.chip_ohcheon -> {
                    // 오천자전거길 선택 시 처리할 작업
                    setupButtonListeners(false, false, false, false, false, false, false, false, false, true, false, false, false)
                    viewModel.fetchLocationPosts("오천 자전거길")
                }
                R.id.chip_kangwon -> {
                    // 동해안강원자전거길 선택 시 처리할 작업
                    setupButtonListeners(false, false, false, false, false, false, false, false, false, false, true, false, false)
                    viewModel.fetchLocationPosts("동해안 자전거길(강원)")
                }
                R.id.chip_kyeongbook -> {
                    // 동해안경북자전거길 선택 시 처리할 작업
                    setupButtonListeners(false, false, false, false, false, false, false, false, false, false, false, true, false)
                    viewModel.fetchLocationPosts("동해안 자전거길(경북)")
                }
                R.id.chip_jeju -> {
                    // 제주자전거길 선택 시 처리할 작업
                    setupButtonListeners(false, false, false, false, false, false, false, false, false, false, false, false, true)
                    viewModel.fetchLocationPosts("제주환상 자전거길")
                }
            }
        }

        // 글쓰기 버튼 클릭 이벤트 처리
        binding.writeButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, WritePostFragment())
            transaction.addToBackStack(null) // 백스택에 추가하여 돌아올 수 있게 설정
            transaction.commit() // 트랜잭션 커밋
        }

        setupObserve()
    }


    // 프래그먼트가 시작될 때 호출 (UI 갱신 관련 작업 등)
    override fun onStart() {
        super.onStart()
        Log.d("CommunityFragment", "onStart 호출됨")
        // 시작될 때 필요한 작업 추가 가능

        // 기본적으로 chipAll이 선택되도록 설정
        setupButtonListeners(true, false, false, false, false, false, false, false, false, false, false, false, false)

    }

    // 프래그먼트가 다시 활성화될 때 호출 (사용자와 상호작용 시작)
    override fun onResume() {
        super.onResume()
        Log.d("CommunityFragment", "onResum 호출됨")
        // 활성화 시 필요한 작업 추가 가능

        setupButtonListeners(true, false, false, false, false, false, false, false, false, false, false, false, false)
    }

    // 프래그먼트가 일시 정지 상태로 들어갈 때 호출 (UI 상태 저장 등)
    override fun onPause() {
        super.onPause()
        Log.d("CommunityFragment", "onPause 호출됨")
        // 일시 정지 시 필요한 작업 추가 가능
    }

    // 프래그먼트가 정지될 때 호출 (리소스 해제 등)
    override fun onStop() {
        super.onStop()
        Log.d("CommunityFragment", "onStop 호출됨")
        // 정지될 때 필요한 작업 추가 가능
    }

    // 프래그먼트의 뷰가 파괴될 때 호출 (뷰와 관련된 리소스 해제)
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("CommunityFragment", "onDestroyView 호출됨")
        // 뷰가 파괴될 때 필요한 작업 추가 가능
    }

    // 프래그먼트 자체가 파괴될 때 호출 (모든 리소스 해제)
    override fun onDestroy() {
        super.onDestroy()
        Log.d("CommunityFragment", "onDestroy 호출됨")
        // 프래그먼트가 완전히 파괴될 때 필요한 작업 추가 가능
//        webSocketManager.closeWebSocket()  // WebSocket 종료
    }

    // 프래그먼트가 부모 액티비티에서 분리될 때 호출
    override fun onDetach() {
        super.onDetach()
        Log.d("CommunityFragment", "onDetach 호출됨")
        // 분리될 때 필요한 작업 추가 가능
    }

    private fun setupObserve() {
        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            // 게시물 데이터가 변경되면 RecyclerView 어댑터에 데이터 전달
            adapter.submitList(posts)
        }
    }

    private fun setupButtonListeners(
        chipAll: Boolean,
        chipAra: Boolean,
        chipHangang: Boolean,
        chipNam: Boolean,
        chipBuk: Boolean,
        chipSaje: Boolean,
        chipNak: Boolean,
        chipKum: Boolean,
        chipYoung: Boolean,
        chipOhcheon: Boolean,
        chipKangwon: Boolean,
        chipKyeongbook: Boolean,
        chipJeju: Boolean
    ) {
        // 각 Chip의 선택 상태를 설정
//        binding.chipAll.isSelected = chipAll
//        binding.chipAra.isSelected = chipAra
//        binding.chipHangang.isSelected = chipHangang
//        binding.chipNam.isSelected = chipNam
//        binding.chipBuk.isSelected = chipBuk
//        binding.chipSaje.isSelected = chipSaje
//        binding.chipNak.isSelected = chipNak
//        binding.chipKum.isSelected = chipKum
//        binding.chipYoung.isSelected = chipYoung
//        binding.chipOhcheon.isSelected = chipOhcheon
//        binding.chipKangwon.isSelected = chipKangwon
//        binding.chipKyeongbook.isSelected = chipKyeongbook
//        binding.chipJeju.isSelected = chipJeju
        binding.chipAll.isChecked = chipAll
        binding.chipAra.isChecked = chipAra
        binding.chipHangang.isChecked = chipHangang
        binding.chipNam.isChecked = chipNam
        binding.chipBuk.isChecked = chipBuk
        binding.chipSaje.isChecked = chipSaje
        binding.chipNak.isChecked = chipNak
        binding.chipKum.isChecked = chipKum
        binding.chipYoung.isChecked = chipYoung
        binding.chipOhcheon.isChecked = chipOhcheon
        binding.chipKangwon.isChecked = chipKangwon
        binding.chipKyeongbook.isChecked = chipKyeongbook
        binding.chipJeju.isChecked = chipJeju
    }

    companion object {
        @JvmStatic
        fun newInstance() = CommunityFragment()
    }
}