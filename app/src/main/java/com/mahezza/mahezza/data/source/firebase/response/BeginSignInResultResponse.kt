package com.mahezza.mahezza.data.source.firebase.response

import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.mahezza.mahezza.common.StringResource

data class BeginSignInResultResponse(
    val isSuccess: Boolean,
    val intentSender: IntentSender?,
    val beginSignInResult: BeginSignInResult?,
    val message : StringResource?,
)