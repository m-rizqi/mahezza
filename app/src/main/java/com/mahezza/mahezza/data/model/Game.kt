package com.mahezza.mahezza.data.model

import androidx.annotation.StringRes
import com.mahezza.mahezza.R
import com.mahezza.mahezza.data.source.firebase.request.GameRequest
import com.mahezza.mahezza.data.source.firebase.response.GameResponse
import com.mahezza.mahezza.data.source.firebase.response.LastGameActivityResponse

data class Game(
    val id : String,
    val parentId : String,
    val children : List<Child>,
    val puzzle : Puzzle,
    val status : Status,
    val lastActivity : String,
    val elapsedTime : String,
    val twibbonUrl : String?,
    val course: Course?
){
    fun toGameRequest(): GameRequest {
        return GameRequest(
            id = this.id,
            parentId = this.parentId,
            childrenIds = this.children.map { it.id },
            puzzleId = this.puzzle.id,
            status = this.status.const,
            lastActivity = this.lastActivity,
            elapsedTime = this.elapsedTime,
            twibbonUrl = this.twibbonUrl,
            course = this.course?.toCourseRequest()
        )
    }

    enum class Status(
        val const : String,
        @StringRes
        val stringResId : Int
    ){
        SelectChild(
            const = "select_child",
            stringResId = R.string.select_child
        ),
        SelectPuzzle(
            const = "select_puzzle",
            stringResId = R.string.select_puzzle
        ),
        PlaySession(
            const = "play_session",
            stringResId = R.string.play
        ),
        TakeTwibbon(
        const = "take_twibbon",
        stringResId = R.string.take_photo
        ),
        Course(
            const = "course",
            stringResId = R.string.story
        ),
        Finished(
            const = "finished",
            stringResId = R.string.finished
        )
    }
}

fun GameResponse.toGame(
    children: List<Child>,
    puzzle: Puzzle
): Game {
    return Game(
        id = this.id,
        parentId = this.parentId,
        children = children,
        puzzle = puzzle,
        status = mapStatusToGameStatus(this.status),
        lastActivity = this.lastActivity,
        elapsedTime = this.elapsedTime,
        twibbonUrl = this.twibbonUrl,
        course = this.course?.toCourse()
    )
}

fun mapStatusToGameStatus(status: String): Game.Status {
    return when(status){
        Game.Status.SelectPuzzle.const -> Game.Status.SelectPuzzle
        Game.Status.PlaySession.const -> Game.Status.PlaySession
        Game.Status.TakeTwibbon.const -> Game.Status.TakeTwibbon
        Game.Status.Course.const -> Game.Status.Course
        else -> Game.Status.SelectChild
    }
}