package com.mahezza.mahezza.data.model

import com.mahezza.mahezza.data.source.firebase.request.UserRequest
import com.mahezza.mahezza.data.source.firebase.response.UserResponse

data class User(
    val id : String,
    val name : String,
    val photoUrl : String,
    val job : String,
    val relationshipWithChildren: String,
    val timeSpendWithChildren : String
)

fun User.toUserRequest(): UserRequest{
    return UserRequest(
        id = this.id,
        name = this.name,
        photoUrl = this.photoUrl,
        job = this.job,
        relationshipWithChildren = this.relationshipWithChildren,
        timeSpendWithChildren = this.timeSpendWithChildren
    )
}

fun UserResponse.toUser() : User {
    return User(
        id = this.id,
        name = this.name,
        photoUrl = this.photoUrl,
        job = this.job,
        relationshipWithChildren = this.relationshipWithChildren,
        timeSpendWithChildren = this.timeSpendWithChildren
    )
}
