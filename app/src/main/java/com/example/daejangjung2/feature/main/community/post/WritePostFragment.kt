package com.example.daejangjung2.feature.main.community.post

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingFragment
import com.example.daejangjung2.data.model.request.CreateRequest
import com.example.daejangjung2.databinding.FragmentCommunityPostBinding
import com.example.daejangjung2.feature.main.community.BikePaths
import kotlinx.coroutines.launch

class WritePostFragment : BindingFragment<FragmentCommunityPostBinding>(R.layout.fragment_community_post) {

//    private val viewModel: WritePostViewModel by viewModels { WritePostViewModel.Factory }
    private val viewModel: WritePostViewModel by viewModels { WritePostViewModel.Factory }

    private var imageUri: Uri? = null

    // 권한 요청 및 갤러리 실행
    private val galleryPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getContent.launch("image/*")
        } else {
            Toast.makeText(requireContext(), "갤러리 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 이미지 선택 실행
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            binding.imageView.setImageURI(uri) // 이미지 뷰에 선택한 이미지 표시
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // AutoCompleteTextView 설정
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, BikePaths.bikePaths)
        binding.autoCompleteLocation.setAdapter(adapter)

        // 이미지 추가 버튼 클릭 리스너 설정
        binding.buttonAddPhoto.setOnClickListener {
            requestImagePermission()
        }

        // 게시물 제출 버튼 클릭 리스너 설정
        binding.buttonSubmit.setOnClickListener {
            submitPost()
        }

        // 툴바 뒤로가기 설정
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack() // 이전 프래그먼트로 이동
        }
    }

    // 갤러리 접근 권한 요청
    private fun requestImagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    // 게시물 제출 로직
    private fun submitPost() {
        val title = binding.editTextTitle.text.toString()
        val content = binding.editTextContent.text.toString()
        val location = binding.autoCompleteLocation.text.toString()
        val imageUrl = imageUri?.toString() ?: "https://i.namu.wiki/i/rhr7FLUilPUcYOaHQxgTQCn3YI0PjjLSXNTQX1v9OOW_dIDQ1HFU1wE_Dl3YLiWQ_WR-tyUAqXq1gHjD8ACAnP6QzKyyIcYdp6szazE6IqMWJJLrQG4wylkWMSQUrYg3z22VhX6FM7SpqMGVpx99Fw.webp"

        if (isValidInput(title, content, location)) {
            val createRequest = CreateRequest(
                title = title,
                contents = content,
                location = location,
                image_url = imageUrl
            )
            Log.d("writepost", "createRequest 생성됨: $createRequest")

            viewModel.submitPost(createRequest)

            // 글 작성 완료 후 이전 Fragment로 돌아감
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
    }

    // 입력 데이터 유효성 검사
    private fun isValidInput(title: String, content: String, location: String): Boolean {
        var isValid = true

        if (title.isEmpty()) {
            binding.editTextTitle.error = "제목을 입력하세요"
            isValid = false
        }
        if (content.isEmpty()) {
            binding.editTextContent.error = "내용을 입력하세요"
            isValid = false
        }
        if (location.isEmpty()) {
            binding.autoCompleteLocation.error = "위치를 입력하세요"
            isValid = false
        } else if (!BikePaths.bikePaths.contains(location)) {
            binding.autoCompleteLocation.error = "올바른 자전거길을 선택하세요"
            isValid = false
        }

        return isValid
    }

    // 생명주기 이벤트 로깅 (선택 사항)
    /*private fun logLifecycle(event: String) {
        android.util.Log.d("WritePostFragment", "$event 호출됨")
    }*/

    override fun onDestroyView() {
        super.onDestroyView()

        // binding이 초기화되지 않았을 경우 null로 설정하지 않도록 방지
    }
}
