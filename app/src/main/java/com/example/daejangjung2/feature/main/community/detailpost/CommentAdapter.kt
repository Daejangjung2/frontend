package com.example.daejangjung2.feature.main.community.detailpost

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.daejangjung2.data.model.response.CommunityComment
import com.example.daejangjung2.databinding.ItemCommentBinding

class CommentAdapter(
    private val viewModel: DetailPostViewModel,
    private val postId: Int
) : ListAdapter<CommunityComment, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CommentViewHolder(private val binding: ItemCommentBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        private var isEditing = false

        fun bind(comment: CommunityComment) {
            binding.commentContent.text = comment.content
            // 댓글 날짜 바인딩
            binding.commentDate.text = formatDate(comment.updateAt)

            // 초기에는 EditText를 숨기고 TextView만 보이게 설정
            binding.commentEdit.visibility = View.GONE
            binding.commentContent.visibility = View.VISIBLE

            binding.btnEdit.setOnClickListener {
                // 수정 모드로 변경
                binding.commentContent.visibility = View.GONE
                binding.commentEdit.setText(comment.content)
                binding.commentEdit.visibility = View.VISIBLE
                binding.btnSave.visibility = View.VISIBLE

                // EditText가 포커스를 얻고 키보드가 올라오도록 설정
                binding.commentEdit.requestFocus()
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.commentEdit, InputMethodManager.SHOW_IMPLICIT)
            }

            binding.btnSave.setOnClickListener {
                // 수정 저장 및 UI 업데이트
                val updatedComment = binding.commentEdit.text.toString()
                binding.commentContent.text = updatedComment
                binding.commentContent.visibility = View.VISIBLE
                binding.commentEdit.visibility = View.GONE
                binding.btnSave.visibility = View.GONE

                // 필요한 경우 서버에 수정된 내용 전달
                viewModel.modifyComment(postId, comment.id, updatedComment)
            }

            // 삭제 버튼 클릭 시
            binding.btnDelete.setOnClickListener {
                // 삭제 버튼 클릭 시 다이얼로그 표시
                showDeleteConfirmationDialog(comment)
            }
        }

        // 삭제 확인 다이얼로그 표시
        private fun showDeleteConfirmationDialog(comment: CommunityComment) {
            AlertDialog.Builder(context).apply {
                setTitle("댓글 삭제")
                setMessage("댓글을 삭제할까요?")
                setPositiveButton("확인") { dialog, _ ->
                    // 확인 버튼 클릭 시 ViewModel의 deleteComment 호출
                    viewModel.deleteComment(postId, comment.id)
                    dialog.dismiss()
                }
                setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }
    }

    // 날짜 형식 변환 함수 추가
    private fun formatDate(dateString: String?): String {
        return if (dateString.isNullOrEmpty()) {
            "날짜 정보 없음"
        } else {
            try {
                val originalFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", java.util.Locale.getDefault())
                val targetFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
                val date = originalFormat.parse(dateString)
                date?.let { targetFormat.format(it) } ?: "날짜 정보 없음"
            } catch (e: Exception) {
                "날짜 형식 오류"
            }
        }
    }

    // DiffUtil 콜백 정의
    class CommentDiffCallback : DiffUtil.ItemCallback<CommunityComment>() {
        override fun areItemsTheSame(oldItem: CommunityComment, newItem: CommunityComment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CommunityComment, newItem: CommunityComment): Boolean {
            return oldItem == newItem
        }
    }
}
