package com.example.daejangjung2.feature.main.mypage

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.daejangjung2.R

class CurvedBackgroundView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.curve_color) // 색상 지정
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val path = Path()
        val width = width.toFloat()
        val height = height.toFloat()

        // 곡선 Path 정의
        path.moveTo(0f, 0f)
        path.lineTo(width, 0f)
        path.lineTo(width, height * 0.7f)
        path.quadTo(width / 2, height, 0f, height * 0.7f)
        path.close()

        // 곡선 그리기
        canvas.drawPath(path, paint)
    }
}