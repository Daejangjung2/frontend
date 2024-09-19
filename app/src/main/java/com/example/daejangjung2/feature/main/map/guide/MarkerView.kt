package com.example.daejangjung2.feature.main.map.guide

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import com.example.daejangjung2.R

class MarkerView(
    context: Context
):View(context) {

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.blue) // 원하는 색상
        alpha = 255 // 투명도 설정
    }
    private var radius = 0f
    private val maxRadius = 200f // 최대 반경

    init {
        // 애니메이션 시작
        startAnimation()
    }

    private fun startAnimation() {
        val animator = ValueAnimator.ofFloat(0f, maxRadius).apply {
            duration = 1000L // 1초 동안
            repeatCount = ValueAnimator.INFINITE // 무한 반복
            repeatMode = ValueAnimator.RESTART
            addUpdateListener { animation ->
                radius = animation.animatedValue as Float
                paint.alpha = (255 * (1 - radius / maxRadius)).toInt() // 반경이 커질수록 투명도 낮춤
                invalidate() // 화면 다시 그리기
            }
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 원 그리기
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }
}