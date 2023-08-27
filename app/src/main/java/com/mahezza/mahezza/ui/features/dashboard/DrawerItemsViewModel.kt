package com.mahezza.mahezza.ui.features.dashboard

import androidx.lifecycle.ViewModel
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.nav.Routes
import com.mahezza.mahezza.ui.theme.RedDanger

class DrawerItemsViewModel : ViewModel() {
    val dashboardDrawerItems = listOf(
        DrawerItem.Menu(
            titleResId = R.string.home,
            iconResId = R.drawable.ic_home,
            route = Routes.Home,
        ),
        DrawerItem.Menu(
            titleResId = R.string.redeem_puzzle,
            iconResId = R.drawable.ic_puzzle,
            route = Routes.Home,
        ),
        DrawerItem.Menu(
            titleResId = R.string.mental_health,
            iconResId = R.drawable.ic_mental_health,
            route = Routes.Home,
        ),
        DrawerItem.Menu(
            titleResId = R.string.notification,
            iconResId = R.drawable.ic_notification,
            route = Routes.Home,
        ),
        DrawerItem.Separator,
        DrawerItem.Menu(
            titleResId = R.string.children,
            iconResId = R.drawable.ic_children,
            route = Routes.Home,
        ),
        DrawerItem.Menu(
            titleResId = R.string.puzzle,
            iconResId = R.drawable.ic_puzzle,
            route = Routes.Home,
        ),
        DrawerItem.Menu(
            titleResId = R.string.profile,
            iconResId = R.drawable.ic_user,
            route = Routes.Home,
        ),
        DrawerItem.Separator,
        DrawerItem.Menu(
            titleResId = R.string.terms_of_service,
            iconResId = R.drawable.ic_terms_of_service,
            route = Routes.Home,
        ),
        DrawerItem.Menu(
            titleResId = R.string.privacy_policy,
            iconResId = R.drawable.ic_privacy_policy,
            route = Routes.Home,
        ),
        DrawerItem.Menu(
            titleResId = R.string.sign_out,
            iconResId = R.drawable.ic_signout,
            selectedColor = RedDanger,
            unSelectedColor = RedDanger,
            onClick = {}
        ),
    )
}