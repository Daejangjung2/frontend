package com.example.daejangjung2.common.view

import androidx.appcompat.app.AppCompatActivity
import com.example.daejangjung2.R
import com.example.daejangjung2.common.view.Toaster

class BackKeyHandler(private val activity: AppCompatActivity, private val time: Long = 2000L) {
    private var backPressedTime = 0L
    fun onBackPressed() {
        if ((System.currentTimeMillis() - backPressedTime) > time) {
            backPressedTime = System.currentTimeMillis()
            Toaster.showShort(activity, activity.getString(R.string.all_back_key_check_message))
        } else {
            activity.finish()
        }
    }
}
