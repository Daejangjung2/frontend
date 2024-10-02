package com.example.daejangjung2.feature.main.community.detailpost

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.daejangjung2.R
import com.example.daejangjung2.databinding.FragmentEditPostBinding
import com.example.daejangjung2.feature.main.community.detailpost.EditPostViewModel

class EditPostFragment : Fragment() {
    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!

    // ViewModel 생성
    private val viewModel: EditPostViewModel by viewModels { EditPostViewModel.Factory }

    private var postId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPostBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // 전달받은 postId를 설정
        postId = arguments?.getInt(ARG_POST_ID) ?: 0

        // 초기 데이터 설정
        setupInitialData()

        // 수정 버튼 클릭 리스너 설정
        binding.buttonEdit.setOnClickListener {
            handleEditPost()
        }

        // 이미지 추가 버튼 클릭 리스너 설정
        binding.buttonAddPhoto.setOnClickListener {
            requestImagePermission()
        }

        // 수정 결과 관찰하여 처리
        viewModel.modifyResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(requireContext(), "게시물이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()  // 수정 성공 시 뒤로 이동
            } else {
                Toast.makeText(requireContext(), "게시물 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    // 게시물 초기 데이터 설정
    private fun setupInitialData() {
        viewModel.modifyPost.observe(viewLifecycleOwner) { post ->
            binding.editTextTitle.setText(post.title)
            binding.editTextContent.setText(post.contents)
            binding.autoCompleteLocation.setText(post.location)

            // Glide를 사용하여 이미지 로드
            Glide.with(requireContext())
                .load(post.image_url)  // 게시물의 이미지 URL 사용
                .placeholder(R.drawable.placeholder_image)  // 로딩 중 기본 이미지
                .into(binding.imageView)

            // ImageView의 태그에 이미지 URL 저장
            binding.imageView.tag = post.image_url
        }
    }

    // 게시물 수정 요청 처리 함수
    private fun handleEditPost() {
        val newTitle = binding.editTextTitle.text.toString()
        val newContents = binding.editTextContent.text.toString()
        val newLocation = binding.autoCompleteLocation.text.toString()
        val newImageUrl = binding.imageView.tag?.toString() ?: ""

        // 필수 항목 체크
        if (newTitle.isNotBlank() && newContents.isNotBlank()) {
            viewModel.updatePost(postId, newTitle, newContents, newLocation, newImageUrl)
        } else {
            Toast.makeText(requireContext(), "제목과 내용을 입력하세요.", Toast.LENGTH_SHORT).show()
        }
    }

    // 권한 요청 및 갤러리 실행 함수 추가
    private val galleryPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 권한이 허용되면 이미지 선택 실행
            getContent.launch("image/*")
        } else {
            // 권한이 거부되었을 경우 사용자에게 알림
            Toast.makeText(requireContext(), "갤러리 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 이미지 선택 실행 (갤러리 열기)
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            binding.imageView.setImageURI(it) // 선택한 이미지를 ImageView에 표시
            binding.imageView.tag = it.toString() // 이미지 URL을 ImageView의 태그로 저장
        }
    }

    // 권한 요청 함수
    private fun requestImagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 이상에서는 READ_MEDIA_IMAGES 권한 사용
            galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            // 그 외 버전에서는 READ_EXTERNAL_STORAGE 권한 사용
            galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    companion object {
        private const val ARG_POST_ID = "post_id"

        fun newInstance(postId: Int) = EditPostFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_POST_ID, postId)  // 전달받은 postId를 Bundle에 저장
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
