package com.mahezza.mahezza.data.source.firebase.request

import com.google.gson.annotations.SerializedName

data class UserRequest(
    val id : String,
    val name : String,
    @SerializedName("photo_url")
    val photoUrl : String
)
