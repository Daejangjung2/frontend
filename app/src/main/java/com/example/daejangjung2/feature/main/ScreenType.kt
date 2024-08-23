package com.example.daejangjung2.feature.main

import androidx.annotation.IdRes
import com.example.daejangjung2.R

sealed class ScreenType(val tag: String, val isChangeBottomTab: Boolean) {
    sealed class ChangeScreenType(tag: String) : ScreenType(tag, true) {
        data object Home : ChangeScreenType("HOME")
        data object Map : ChangeScreenType("MAP")
        data object Community: ChangeScreenType("COMMUNITY");
        data object MyPage : ChangeScreenType("MY_PAGE")
    }

    sealed class PopScreenType(tag: String) : ScreenType(tag, false) {
        data object Category : PopScreenType("CATEGORY")
    }

    companion object {
        fun of(@IdRes id: Int): ScreenType {
            return when (id) {
                R.id.menu_item_home -> ChangeScreenType.Home
                R.id.menu_item_map -> ChangeScreenType.Map
                R.id.menu_item_community -> ChangeScreenType.Community
                R.id.menu_item_my_page -> ChangeScreenType.MyPage
                else -> ChangeScreenType.Home
            }
        }
    }
}