package com.example.daejangjung2.common.view

import android.view.View
import androidx.annotation.StringRes
import com.example.daejangjung2.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    @StringRes
    textId: Int,
    @StringRes
    actionId: Int = R.string.all_snackbar_default_action,
    anchor: View? = null,
    action: () -> Unit = {},
): Snackbar {
    return Snackbar.make(this, context.getString(textId), Snackbar.LENGTH_LONG)
        .apply {
            animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
            anchor?.let { anchorView = anchor }
        }
        .setTextColor(resources.getColor(R.color.black_000000, null))
        .setActionTextColor(resources.getColor(R.color.black_000000, null))
        .setBackgroundTint(resources.getColor(R.color.vista_blue_469CFF, null))
        .setAction(context.getString(actionId)) {
            action()
        }
}

fun View.showSnackbar(
    message: String,
    actionMessage: String,
    anchor: View? = null,
    action: () -> Unit = {},
): Snackbar {
    return Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .apply {
            animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
            anchor?.let { anchorView = anchor }
        }
        .setTextColor(resources.getColor(R.color.black_000000, null))
        .setActionTextColor(resources.getColor(R.color.black_000000, null))
        .setBackgroundTint(resources.getColor(R.color.vista_blue_469CFF, null))
        .setAction(actionMessage) {
            action()
        }
}
