package com.mahezza.mahezza.data.source.firebase.response

import com.google.gson.annotations.SerializedName

data class ChildResponse(
    @SerializedName("parent_id")
    val parentId : String = "",
    val id : String = "",
    val name : String = "",
    val gender : String = "",
    val birthdate : String = "",
    @SerializedName("photo_url")
    val photoUrl : String = ""
)
