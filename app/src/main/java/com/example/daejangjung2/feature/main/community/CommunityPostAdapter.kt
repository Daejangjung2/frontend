package com.example.daejangjung2.feature.main.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.daejangjung2.databinding.ItemCommunityPostBinding

class CommunityPostAdapter : ListAdapter<CommunityPost, CommunityPostAdapter.CommunityPostViewHolder>(DiffCallback()) {

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
        fun bind(post: CommunityPost) {
            binding.postTitle.text = post.title
            binding.postContent.text = post.content
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CommunityPost>() {
        override fun areItemsTheSame(oldItem: CommunityPost, newItem: CommunityPost): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: CommunityPost, newItem: CommunityPost): Boolean {
            return oldItem == newItem
        }
    }
}
