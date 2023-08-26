package com.mahezza.mahezza.data.source.firebase.response

import android.net.Uri
import com.mahezza.mahezza.common.StringResource

data class SignInResult(
    val data : UserData?,
    val errorMessage : StringResource?
) {
    data class UserData(
        val uid : String,
        val displayName : String,
        val photoUrl : Uri
    )
}
