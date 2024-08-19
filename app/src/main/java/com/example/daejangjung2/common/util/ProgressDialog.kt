package com.example.daejangjung2.util.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.annotation.RawRes
import com.example.daejangjung2.databinding.ProgressdialogBinding

class ProgressDialog(context: Context) : Dialog(context) {
    private val binding: ProgressdialogBinding = ProgressdialogBinding.inflate(layoutInflater)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        setCancelable(false)
    }

    fun startWithAnimation(@RawRes rawResId: Int) {
        binding.lavProgress.setAnimation(rawResId)
        this.show()
    }

    override fun onStart() {
        super.onStart()
        binding.lavProgress.playAnimation()
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun dismiss() {
        super.dismiss()
        binding.lavProgress.pauseAnimation()
    }
}
