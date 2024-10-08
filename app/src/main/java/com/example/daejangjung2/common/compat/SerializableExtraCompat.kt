package com.example.daejangjung2.common.compat

import android.content.Intent
import android.os.Build
import java.io.Serializable

@Suppress("DEPRECATION")
inline fun <reified T : Serializable> Intent.getSerializableExtraCompat(
    key: String,
): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(key, T::class.java)
    } else {
        getSerializableExtra(key) as? T
    }
}
