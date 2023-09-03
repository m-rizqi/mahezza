package com.mahezza.mahezza.ui.features.game.twibbon

import android.net.Uri
import com.mahezza.mahezza.common.StringResource

data class TakeTwibbonUiState(
    val photoUri : Uri? = null,

    val isShowLoading : Boolean = false,
    val generalMessage : StringResource? = null
)