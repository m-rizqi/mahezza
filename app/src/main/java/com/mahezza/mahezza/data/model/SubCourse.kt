package com.mahezza.mahezza.data.model

import com.mahezza.mahezza.data.source.firebase.request.ContentRequest
import com.mahezza.mahezza.data.source.firebase.request.SubCourseRequest
import com.mahezza.mahezza.data.source.firebase.response.ContentResponse
import com.mahezza.mahezza.data.source.firebase.response.SubCourseResponse

data class SubCourse(
    val name : String,
    val id : String,
    val contents : List<Content>,
    val numberOfChallenges : Int,
    val illustrationUrl : String?,

    val progress : Float = 0.0f,
    val isCompleted : Boolean = false,
    val numberOfCompletedChallenges : Int = 0,
)

fun SubCourseResponse.toSubCourse() : SubCourse = SubCourse(
    name = this.name,
    numberOfChallenges = this.numberOfChallenges,
    id = this.subCourseId,
    contents = mapContentResponsesToContents(this.content),
    illustrationUrl = this.illustrationUrl
)

private fun mapContentResponsesToContents(contentResponses: List<ContentResponse>) : List<Content> {
    val contents = mutableListOf<Content>()
    contentResponses.forEach {contentResponse ->
        with(contentResponse){
            challenge?.let { contents.add(it.toChallenge()) }
            script?.let { contents.add(it.toScript()) }
            image?.let { contents.add(it.toImage()) }
            video?.let { contents.add(it.toVideo()) }
        }
    }
    return contents
}

fun SubCourse.toSubCourseRequest() : SubCourseRequest = SubCourseRequest(
    content = mapContentToContentRequests(this.contents),
    name = this.name,
    numberOfChallenges = this.numberOfChallenges,
    subCourseId = this.id,
    illustrationUrl = this.illustrationUrl,

    progress = this.progress,
    isCompleted = this.isCompleted,
    numberOfCompletedChallenges = this.numberOfCompletedChallenges,
)

private fun mapContentToContentRequests(contents: List<Content>) : List<ContentRequest> {
    val contentRequests = mutableListOf<ContentRequest>()
    contents.forEach {content ->
        when(content){
            is Content.Challenge -> contentRequests.add(ContentRequest(challenge = content.toChallengeOfContentRequest()))
            is Content.Image -> contentRequests.add(ContentRequest(image = content.toImageOfContentRequest()))
            is Content.Script -> contentRequests.add(ContentRequest(script = content.toScriptOfContentRequest()))
            is Content.Video -> contentRequests.add(ContentRequest(video = content.toVideoOfContentRequest()))
        }
    }
    return contentRequests
}