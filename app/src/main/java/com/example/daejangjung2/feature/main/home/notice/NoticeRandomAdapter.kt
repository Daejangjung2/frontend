package com.example.daejangjung2.feature.main.home.notice

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.daejangjung2.R
import com.example.daejangjung2.domain.model.Notice

class NoticeRandomAdapter(
    private val context: Context,
    private var items: List<Notice>,
    val onNoticeClick: (Notice) -> Unit
): RecyclerView.Adapter<NoticeRandomAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.thumbnail_image)
        val textView: TextView = itemView.findViewById(R.id.thumbnail_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notice_home_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val screenWidth = context.resources.displayMetrics.widthPixels
        val spacing = context.resources.displayMetrics.density * 16  // 8dp의 간격 고려
        val itemWidth = (screenWidth * 0.75).toInt() - spacing.toInt()
        holder.itemView.layoutParams.width = itemWidth

        // 데이터 바인딩
        val item = items[position]
        holder.textView.text = item.comment
        Glide.with(context)
            .load(item.image)
            .placeholder(R.drawable.ic_map_pin_user_24) // 로딩 중일 때 보여줄 이미지
            .error(R.drawable.ic_map_open_store_24) // 에러 발생 시 보여줄 이미지
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            onNoticeClick(item)
        }
    }
}

class HorizontalSpaceItemDecoration(private val horizontalSpaceWidth: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = horizontalSpaceWidth

        // 첫 번째 아이템이 아닌 경우에만 왼쪽 간격 추가
        val position = parent.getChildAdapterPosition(view)
        if (position != 0) {
            outRect.left = horizontalSpaceWidth
        }
    }
}