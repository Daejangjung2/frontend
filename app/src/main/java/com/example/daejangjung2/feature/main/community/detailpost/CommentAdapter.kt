package com.example.daejangjung2.feature.main.community.detailpost

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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

        fun bind(comment: CommunityComment) {
            binding.commentContent.text = comment.content

            // 수정 버튼 클릭 시의 동작
            binding.btnEdit.setOnClickListener {
                // 수정 버튼 클릭 시의 동작 구현 (원하는 로직을 여기에 추가)
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
