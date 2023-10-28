package com.mahezza.mahezza.data.model

import com.mahezza.mahezza.data.source.firebase.request.ContentRequest
import com.mahezza.mahezza.data.source.firebase.response.ContentResponse

sealed class Content(
    val id : String?,
    val content : String,
    val title : String?,
    val isCompleted : Boolean?,
    val challengeNumber : Int?,
    val numberOfChallenges : Int?,
    val type : String,
) {
    class Challenge(
        id: String,
        content: String,
        title: String,
        isCompleted: Boolean,
        challengeNumber: Int,
        numberOfChallenges: Int,
    ) : Content(id, content, title, isCompleted, challengeNumber, numberOfChallenges, Type.CHALLENGE)
    class Script(
        id: String?,
        content: String,
        title: String?,
    ) : Content(id, content, title, null, null, null, type = Type.SCRIPT)
    class Image(
        id: String?,
        content: String,
        title: String?,
    ) : Content(id, content, title, null, null, null, type = Type.IMAGE)
    class Video(
        id: String?,
        content: String,
        title: String?,
    ) : Content(id, content, title, null, null, null, type = Type.VIDEO)

    companion object Type {
        const val SCRIPT = "script"
        const val CHALLENGE = "challenge"
        const val IMAGE = "image"
        const val VIDEO = "video"
    }
}

fun ContentResponse.toContent() : Content {
    return when(this.type){
        Content.SCRIPT -> Content.Script(this.id, content = this.content, title = this.title)
        Content.CHALLENGE -> Content.Challenge(
            id = this.id ?: "",
            content = this.content ?: "",
            title = this.title ?: "",
            isCompleted = this.isCompleted ?: false,
            challengeNumber = this.challengeNumber ?: 0,
            numberOfChallenges = this.numberOfChallenges ?: 0
        )
        Content.IMAGE -> Content.Image(
            id = this.id,
            content = this.content,
            title = this.title
        )
        Content.VIDEO -> Content.Video(
            id = this.id,
            content = this.content,
            title = this.title
        )
        else -> Content.Script(
            id = this.id,
            content = this.content,
            title = this.title
        )
    }
}

fun Content.toContentRequest() : ContentRequest {
    return ContentRequest(
        id = id,
        content = content,
        title = title,
        isCompleted = isCompleted,
        challengeNumber = challengeNumber,
        numberOfChallenges = numberOfChallenges,
        type = type
    )
}