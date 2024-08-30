package com.example.daejangjung2.feature.main.home.medal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.daejangjung2.R
import com.example.daejangjung2.domain.model.Medal

class MedalAdapter(
    private val context: Context,
    private var items: ArrayList<Medal>
):RecyclerView.Adapter<MedalAdapter.MedalViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medal_list, parent, false)
        return MedalViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedalViewHolder, position: Int) {
        val item = items[position];

        Glide.with(context)
            .load(R.drawable.medal)
            .into(holder.medalImage)
        holder.medalText.text = item.comment
    }

    fun updateList(list: ArrayList<Medal>){
        items = list
        notifyDataSetChanged()
    }

    //items의 갯수를 반환하여 RecyclerView에 나타나는 총 갯수를 보여줌
    override fun getItemCount(): Int {
        return items.size
    }

    inner class MedalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val medalImage = itemView.findViewById<ImageView>(R.id.medal_images)
        val medalText = itemView.findViewById<TextView>(R.id.medal_text)
    }
}