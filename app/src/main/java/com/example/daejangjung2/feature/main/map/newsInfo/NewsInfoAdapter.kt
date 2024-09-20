package com.example.daejangjung2.feature.main.map.newsInfo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.daejangjung2.R
import com.example.daejangjung2.domain.model.NewsInfo

class NewsInfoAdapter(
    private val context: Context,
    private var items: List<NewsInfo>
):RecyclerView.Adapter<NewsInfoAdapter.NewsInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent,false);
        return NewsInfoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NewsInfoViewHolder, position: Int) {
        val item = items[position]

        holder.newsTitle.text = item.title
        holder.newsContent.text = item.title
        Glide.with(context)
            .load(R.drawable.sample_image)
            .into(holder.newsImage)
    }

    inner class NewsInfoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val newsTitle = itemView.findViewById<TextView>(R.id.news_title)
        val newsContent = itemView.findViewById<TextView>(R.id.news_content)
        val newsImage = itemView.findViewById<ImageView>(R.id.news_image)
    }
}