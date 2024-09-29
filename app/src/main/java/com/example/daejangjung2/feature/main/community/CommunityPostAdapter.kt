package com.example.daejangjung2.feature.main.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.daejangjung2.R
import com.example.daejangjung2.data.model.response.PostCallAllResponse
import com.example.daejangjung2.data.model.response.PostContent
import com.example.daejangjung2.databinding.ItemCommunityPostBinding

// CommunityPostAdapter 정의
class CommunityPostAdapter(
    private val onPostClick: (PostCallAllResponse) -> Unit  // 클릭 이벤트를 람다로 정의
) : ListAdapter<PostCallAllResponse, CommunityPostAdapter.CommunityPostViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityPostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCommunityPostBinding.inflate(inflater, parent, false)
        return CommunityPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommunityPostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
        holder.itemView.setOnClickListener {
            onPostClick(post)  // 클릭 시 콜백 호출
        }
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

// LocationPostAdapter 정의
class LocationPostAdapter(
    private val onPostClick: (PostContent) -> Unit  // 클릭 이벤트를 람다로 정의
) : ListAdapter<PostContent, LocationPostAdapter.LocationPostViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationPostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCommunityPostBinding.inflate(inflater, parent, false)
        return LocationPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationPostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
        holder.itemView.setOnClickListener {
            onPostClick(post)  // 클릭 시 콜백 호출
        }
    }

    class LocationPostViewHolder(private val binding: ItemCommunityPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostContent) {
            binding.postTitle.text = post.title ?: "No Title"
            binding.postLocation.text = post.location ?: "No Content"

            // Load image with Glide, showing placeholder if no image URL
            Glide.with(binding.postImage.context)
                .load(post.image_url ?: R.drawable.placeholder_image)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.postImage)

            binding.executePendingBindings() // Execute pending bindings immediately
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PostContent>() {
        override fun areItemsTheSame(oldItem: PostContent, newItem: PostContent): Boolean {
            return oldItem.postId == newItem.postId  // Check if items represent the same post by comparing their IDs
        }

        override fun areContentsTheSame(oldItem: PostContent, newItem: PostContent): Boolean {
            return oldItem == newItem  // Check if the content of items is identical
        }
    }
}
