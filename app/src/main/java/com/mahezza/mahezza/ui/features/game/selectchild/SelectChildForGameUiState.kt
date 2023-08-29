package com.mahezza.mahezza.ui.features.game.selectchild

import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.ui.components.LayoutState

data class SelectChildForGameUiState(
    val isShowLoading : Boolean = false,
    val generalMessage : StringResource? = null,

    val selectChildCardForGameStateList: List<SelectChildCardForGameState> = emptyList(),
    val children : List<Child> = emptyList(),
    val finalSelectedChildren : List<Child>? = null,

    val childLayoutState : LayoutState = LayoutState.Shimmer
){
    data class SelectChildCardForGameState(
        val id : String,
        val imageUrl : String,
        val name : String,
        val age : Double,
        val lastActivity : String,
        val isChecked : Boolean,
        val onCheckedChangeListener : (Boolean) -> Unit
    )
}
