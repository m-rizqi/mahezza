package com.mahezza.mahezza.ui.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.domain.Result
import com.mahezza.mahezza.domain.auth.LogOutUseCase
import com.mahezza.mahezza.ui.nav.Routes
import com.mahezza.mahezza.ui.theme.RedDanger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawerItemsViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase
) : ViewModel() {

    private val _generalMessage = MutableStateFlow<StringResource?>(null)
    val generalMessage : StateFlow<StringResource?>
        get() = _generalMessage.asStateFlow()

    private val _isLogOut = MutableStateFlow(false)
    val isLogOut : StateFlow<Boolean>
        get() = _isLogOut.asStateFlow()

    private val _isShowLoading = MutableStateFlow(false)
    val isShowLoading : StateFlow<Boolean>
        get() = _isShowLoading.asStateFlow()


    val dashboardDrawerItems = listOf(
        DrawerItem.Menu(
            titleResId = R.string.home,
            iconResId = R.drawable.ic_home,
            route = Routes.Home,
        ),
        DrawerItem.Menu(
            titleResId = R.string.redeem_puzzle,
            iconResId = R.drawable.ic_puzzle,
            route = Routes.RedeemPuzzle,
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
            onClick = {
                logOut()
            }
        ),
    )

    fun onGeneralMessageShowed(){
        _generalMessage.update { null }
    }

    private fun logOut() {
        viewModelScope.launch {
            _isShowLoading.update { true }
            val result = logOutUseCase()
            when(result){
                is Result.Fail -> _generalMessage.update { result.message }
                is Result.Success -> _isLogOut.update { true }
            }
            _isShowLoading.update { false }
        }
    }
}