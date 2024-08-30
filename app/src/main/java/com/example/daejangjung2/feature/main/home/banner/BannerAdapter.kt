package com.example.daejangjung2.feature.main.home.banner

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.daejangjung2.R
import com.example.daejangjung2.domain.model.ImageBanner

class BannerAdapter(private val context: Context, private val images: ArrayList<ImageBanner>) :
    RecyclerView.Adapter<BannerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.banner_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("BannerAdapter", "Loading image: ${images[position].image}")
        Glide.with(context)
            .load(images[position].image)
            .override(360, 360)
            .placeholder(R.drawable.ic_map_pin_user_24) // 로딩 중일 때 보여줄 이미지
            .error(R.drawable.ic_map_open_store_24) // 에러 발생 시 보여줄 이미지
            .into(holder.image)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById<ImageView>(R.id.banner_image)
    }
}