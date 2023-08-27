package com.mahezza.mahezza.ui.features.dashboard

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.GreyText

sealed class DrawerItem {
    data class Menu(
        @StringRes
        val titleResId : Int,
        @DrawableRes
        val iconResId : Int,
        val badgeCount : Int? = null,
        val selectedColor : Color = Black,
        val unSelectedColor : Color = GreyText,
        val route : String? = null,
        val onClick : () -> Unit = {}
    ) : DrawerItem()
    object Separator : DrawerItem()
}