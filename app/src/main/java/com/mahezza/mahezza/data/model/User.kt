package com.mahezza.mahezza.data.model

import com.mahezza.mahezza.data.source.firebase.request.UserRequest

data class User(
    val id : String,
    val name : String,
    val photoUrl : String
)

fun User.toUserRequest(): UserRequest{
    return UserRequest(
        id = this.id,
        name = this.name,
        photoUrl = this.photoUrl
    )
}
