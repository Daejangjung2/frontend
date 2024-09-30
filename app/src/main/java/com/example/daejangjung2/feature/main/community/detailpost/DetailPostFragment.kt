package com.example.daejangjung2.feature.main.community.detailpost

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.daejangjung2.R
import com.example.daejangjung2.databinding.FragmentPostDetailBinding

class DetailPostFragment : Fragment() {
    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    // ViewModel 생성
    private val viewModel: DetailPostViewModel by viewModels { DetailPostViewModel.Factory }

    private lateinit var commentAdapter: CommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        val postId = arguments?.getInt(ARG_POST_ID) ?: 0

        // RecyclerView 설정
        commentAdapter = CommentAdapter(viewModel, postId)
        binding.commentRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commentAdapter
        }

        // ViewModel을 통해 데이터를 가져오고 UI에 업데이트
        viewModel.loadDetailPost(postId)
        setupObserve()

        // 댓글 추가 버튼 클릭 리스너 설정
        binding.sendCommentButton.setOnClickListener {
            val commentText = binding.commentEditText.text.toString()
            if (commentText.isNotBlank()) {
                // 댓글 추가 API 호출
                viewModel.addComment(postId, commentText)
                // 키보드 숨기기
                hideKeyboard()
                // 댓글 입력란 초기화
                binding.commentEditText.text.clear()
            }
        }

        // moreButton 클릭 시 수정 및 삭제 옵션 다이얼로그 표시
        binding.moreButton.setOnClickListener {
            showMoreOptionsDialog(postId)
        }

        return binding.root
    }

    private fun setupObserve() {
        viewModel.detailPost.observe(viewLifecycleOwner) { post ->
            binding.postTitle.text = post.title
            binding.postContent.text = post.contents
            binding.postLocation.text = post.location

            // Glide로 이미지 로드
            if (post.image_url != null) {
                Glide.with(binding.postImage.context)
                    .load(post.image_url)
                    .placeholder(R.drawable.placeholder_image) // 로딩 중 기본 이미지
                    .into(binding.postImage)
            } else {
                // 이미지가 없는 경우 기본 이미지 설정
                binding.postImage.setImageResource(R.drawable.placeholder_image)
            }

            // 댓글 리스트가 변경될 때마다 RecyclerView 업데이트
            post.communityComment?.let { comments ->
                commentAdapter.submitList(comments)
            }
        }

        viewModel.comments.observe(viewLifecycleOwner) { comments ->
            commentAdapter.submitList(comments)
        }

        // 삭제 결과 관찰하여 처리
        viewModel.deleteResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "게시물이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            } else {
                Toast.makeText(requireContext(), "게시물을 삭제할 수 없습니다. 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

//        // 수정 결과 관찰하여 처리
//        viewModel.updateResult.observe(viewLifecycleOwner) { success ->
//            if (success) {
//                Toast.makeText(requireContext(), "게시물이 수정되었습니다.", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(requireContext(), "게시물 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    private fun showMoreOptionsDialog(postId: Int) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("게시물 관리")
            setItems(arrayOf("게시물 수정", "게시물 삭제")) { _, which ->
                when (which) {
                    0 -> showUpdatePostDialog(postId)
                    1 -> showDeleteConfirmationDialog(postId)
                }
            }
            create()
            show()
        }
    }

    // 게시물 수정 다이얼로그 표시
    private fun showUpdatePostDialog(postId: Int) {
        val titleEditText = EditText(requireContext()).apply {
            hint = "수정할 제목을 입력하세요"
        }
        val contentEditText = EditText(requireContext()).apply {
            hint = "수정할 내용을 입력하세요"
        }
        val locationEditText = EditText(requireContext()).apply {
            hint = "수정할 위치를 입력하세요"
        }
        val imageUrlEditText = EditText(requireContext()).apply {
            hint = "수정할 이미지 URL을 입력하세요"
        }

        AlertDialog.Builder(requireContext()).apply {
            setTitle("게시물 수정")
            setView(LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                addView(titleEditText)
                addView(contentEditText)
                addView(locationEditText)
                addView(imageUrlEditText)
            })
            setPositiveButton("수정") { dialog, _ ->
                val newTitle = titleEditText.text.toString()
                val newContents = contentEditText.text.toString()
                val newLocation = locationEditText.text.toString()
                val newImageUrl = imageUrlEditText.text.toString()
                if (newTitle.isNotBlank() && newContents.isNotBlank()) {
                    viewModel.updatePost(postId, newTitle, newContents, newLocation, newImageUrl)
                    Toast.makeText(requireContext(), "게시물이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "빈 칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            setNegativeButton("취소") { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

    // 삭제
    private fun showDeleteConfirmationDialog(postId: Int) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("게시물 삭제")
            setMessage("게시물을 삭제하시겠습니까?")
            setPositiveButton("삭제") { dialog, _ ->
                viewModel.deletePost(postId)
                dialog.dismiss()
            }
            setNegativeButton("취소") { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 키보드 숨기기 함수
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.commentEditText.windowToken, 0)
        // EditText에서 포커스 해제
        binding.commentEditText.clearFocus()
    }

    companion object {
        private const val ARG_POST_ID = "post_id"

        fun newInstance(postId: Int) = DetailPostFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_POST_ID, postId)  // 전달받은 postId를 Bundle에 저장
            }
        }
    }
}
