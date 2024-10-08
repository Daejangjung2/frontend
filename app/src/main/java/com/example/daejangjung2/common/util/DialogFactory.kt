package com.example.daejangjung2.util.common

import android.app.Activity
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.daejangjung2.R

fun Activity.showDialog(
    @StringRes
    titleId: Int? = null,
    @StringRes
    messageId: Int,
    @StringRes
    negativeStringId: Int? = null,
    @StringRes
    positiveStringId: Int = R.string.all_dialog_default_positive_button,
    actionNegative: () -> Unit = {},
    actionPositive: () -> Unit = {},
    isCancelable: Boolean = true,
) {
    AlertDialog.Builder(this).apply {
        titleId?.let { setTitle(getString(it)) }
        setMessage(getString(messageId))
        negativeStringId?.let {
            setNegativeButton(negativeStringId) { _, _ ->
                actionNegative()
            }
        }
        setPositiveButton(positiveStringId) { _, _ ->
            actionPositive()
        }
        setCancelable(isCancelable)
    }.show()
}

fun Fragment.showDialog(
    @StringRes
    titleId: Int,
    @StringRes
    messageId: Int,
    @StringRes
    negativeStringId: Int,
    @StringRes
    positiveStringId: Int,
    actionNegative: () -> Unit = {},
    actionPositive: () -> Unit = {},
) {
    AlertDialog.Builder(requireContext())
        .setTitle(getString(titleId))
        .setMessage(getString(messageId))
        .setNegativeButton(negativeStringId) { _, _ ->
            actionNegative()
        }
        .setPositiveButton(positiveStringId) { _, _ ->
            actionPositive()
        }
        .show()
}
