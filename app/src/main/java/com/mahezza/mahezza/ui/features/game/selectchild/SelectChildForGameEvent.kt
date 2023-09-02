package com.mahezza.mahezza.ui.features.game.selectchild

sealed class SelectChildForGameEvent {
    class OnCheckedChildChanged(val childId : String, val isChecked : Boolean) : SelectChildForGameEvent()
    object OnGeneralMessageShowed : SelectChildForGameEvent()
    object OnNextListener : SelectChildForGameEvent()
    object OnNavigatedToSelectPuzzle : SelectChildForGameEvent()
}