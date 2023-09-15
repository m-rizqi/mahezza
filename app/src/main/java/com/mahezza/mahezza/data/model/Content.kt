package com.mahezza.mahezza.data.model

import com.mahezza.mahezza.data.source.firebase.request.ChallengeOfContentRequest
import com.mahezza.mahezza.data.source.firebase.request.ImageOfContentRequest
import com.mahezza.mahezza.data.source.firebase.request.ScriptOfContentRequest
import com.mahezza.mahezza.data.source.firebase.request.VideoOfContentRequest
import com.mahezza.mahezza.data.source.firebase.response.ChallengeOfContentResponse
import com.mahezza.mahezza.data.source.firebase.response.ImageOfContentResponse
import com.mahezza.mahezza.data.source.firebase.response.ScriptOfContentResponse
import com.mahezza.mahezza.data.source.firebase.response.VideoOfContentResponse

sealed class Content(
    open val position : Int = 0
) {
    data class Challenge(
        override val position: Int,
        val id : String,
        val instruction : String,
        val title : String,

        var isCompleted: Boolean = false,
        val challengeNumber : Int = 0,
        val numberOfChallenges: Int = 0,
    ) : Content(position = position)
    data class Video(
        override val position: Int,
        val url : String,
    ) : Content(position = position)
    data class Image(
        override val position: Int,
        val url : String,
    ) : Content(position = position)
    data class Script(
        override val position: Int,
        val text : String,
    ) : Content(position = position)
}

fun ChallengeOfContentResponse.toChallenge() : Content.Challenge = Content.Challenge(
    id = this.challengeId,
    instruction = this.instruction,
    title = this.title,
    position = this.position
)
fun VideoOfContentResponse.toVideo() : Content.Video = Content.Video(
    position = this.position,
    url = this.url
)
fun ImageOfContentResponse.toImage() : Content.Image = Content.Image(
    position = this.position,
    url = this.url
)
fun ScriptOfContentResponse.toScript() : Content.Script = Content.Script(
    position = this.position,
    text = this.text
)

fun Content.Challenge.toChallengeOfContentRequest() : ChallengeOfContentRequest = ChallengeOfContentRequest(
    position = this.position,
    challengeId = this.id,
    instruction = this.instruction,
    title = this.title,
    completed = this.isCompleted,
    challengeNumber = this.challengeNumber,
    numberOfChallenges = this.numberOfChallenges,
)

fun Content.Video.toVideoOfContentRequest() : VideoOfContentRequest = VideoOfContentRequest(
    position = this.position,
    url = this.url
)

fun Content.Image.toImageOfContentRequest() : ImageOfContentRequest = ImageOfContentRequest(
    position = this.position,
    url = this.url
)

fun Content.Script.toScriptOfContentRequest() : ScriptOfContentRequest = ScriptOfContentRequest(
    position = this.position,
    text = this.text
)