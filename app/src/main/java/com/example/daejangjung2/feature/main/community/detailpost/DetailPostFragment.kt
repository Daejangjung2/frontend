package com.example.daejangjung2.feature.main.community.detailpost

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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

        // moreButton 클릭 시 팝업 메뉴 표시
        binding.moreButton.setOnClickListener { showPopupMenu(it, postId) }

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
    }

    // 팝업 메뉴 표시 함수
    private fun showPopupMenu(view: View, postId: Int) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.post_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_edit_post -> {
                    // 게시물 수정 이벤트 처리
//                    editPost(postId)
                    true
                }
                R.id.action_delete_post -> {
                    // 게시물 삭제 이벤트 처리
//                    showDeleteConfirmationDialog(postId)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
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
