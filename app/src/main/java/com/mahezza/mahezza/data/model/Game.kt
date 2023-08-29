package com.mahezza.mahezza.data.model

import androidx.annotation.StringRes
import com.mahezza.mahezza.R
import com.mahezza.mahezza.data.source.firebase.request.GameRequest

data class Game(
    val id : String,
    val parentId : String,
    val children : List<Child>,
    val puzzle : Puzzle,
    val status : Status,
    val lastActivity : String
){
    fun toGameRequest(): GameRequest {
        return GameRequest(
            id = this.id,
            parentId = this.parentId,
            childrenIds = this.children.map { it.id },
            puzzleId = this.puzzle.id,
            status = this.status.const,
            lastActivity = this.lastActivity
        )
    }

    enum class Status(
        val const : String,
        @StringRes
        val stringResId : Int
    ){
        OnGoing(
            const = "on_going",
            stringResId = R.string.on_going
        ),
        Finished(
            const = "finished",
            stringResId = R.string.finished
        ),
        Paused(
            const = "paused",
            stringResId = R.string.paused
        )
    }
}
