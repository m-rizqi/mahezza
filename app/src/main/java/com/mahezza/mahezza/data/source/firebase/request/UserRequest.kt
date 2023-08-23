package com.mahezza.mahezza.data.source.firebase.request

import com.google.gson.annotations.SerializedName

data class UserRequest(
    val id : String,
    val name : String,
    @SerializedName("photo_url")
    val photoUrl : String,
    val job : String,
    @SerializedName("relationship_with_children")
    val relationshipWithChildren: String,
    @SerializedName("time_spend_with_children")
    val timeSpendWithChildren : String
)
