package com.example.daejangjung2.feature.main.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.daejangjung2.R
import com.example.daejangjung2.data.model.response.PostCallAllResponse
import com.example.daejangjung2.databinding.ItemCommunityPostBinding

class CommunityPostAdapter : ListAdapter<PostCallAllResponse, CommunityPostAdapter.CommunityPostViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityPostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCommunityPostBinding.inflate(inflater, parent, false)
        return CommunityPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommunityPostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class CommunityPostViewHolder(private val binding: ItemCommunityPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostCallAllResponse) {
            binding.postTitle.text = post.title ?: "No Title"  // 제목이 없을 경우 기본값 설정
            binding.postLocation.text = post.location ?: "No Content"  // 내용이 없을 경우 기본값 설정

            // Glide로 이미지 로드
            Glide.with(binding.postImage.context)
                .load(post.image_url ?: R.drawable.placeholder_image)  // 이미지 URL이 없을 경우 기본 이미지 사용
                .placeholder(R.drawable.placeholder_image)  // 로딩 중 기본 이미지 설정
                .into(binding.postImage)

            binding.executePendingBindings() // 즉시 바인딩 실행
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PostCallAllResponse>() {
        override fun areItemsTheSame(oldItem: PostCallAllResponse, newItem: PostCallAllResponse): Boolean {
            return oldItem.postId == newItem.postId // 각 게시물의 ID가 같을 경우 같은 아이템으로 간주
        }

        override fun areContentsTheSame(oldItem: PostCallAllResponse, newItem: PostCallAllResponse): Boolean {
            return oldItem == newItem // 전체 내용을 비교하여 같은지 확인
        }
    }
}
