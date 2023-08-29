package com.mahezza.mahezza.ui.features.game

import com.mahezza.mahezza.data.model.Child

sealed class GameEvent {
    class SetSelectedChildren(val children : List<Child>) : GameEvent()
}
