package com.mahezza.mahezza.data.model

import com.mahezza.mahezza.data.source.firebase.request.ChildRequest
import com.mahezza.mahezza.data.source.firebase.response.ChildResponse

data class Child(
    val parentId : String,
    val id : String,
    val name : String,
    val gender : String,
    val birthdate : String,
    val photoUrl : String,
    val lastActivity : String
)

fun Child.toChildRequest(): ChildRequest {
    return ChildRequest(
        parentId = this.parentId,
        id = this.id,
        name = this.name,
        gender = this.gender,
        birthdate = this.birthdate,
        photoUrl = this.photoUrl
    )
}

fun ChildResponse.toChild() : Child {
    return Child(
        parentId = this.parentId,
        id = this.id,
        name = this.name,
        gender = this.gender,
        birthdate = this.birthdate,
        photoUrl = this.photoUrl,
        lastActivity = this.lastActivity
    )
}
