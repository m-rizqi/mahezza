package com.mahezza.mahezza.domain.auth

import android.content.Intent
import com.mahezza.mahezza.data.source.firebase.response.BeginSignInResultResponse
import com.mahezza.mahezza.domain.Result

interface LoginWithGoogleUseCase {
    suspend fun beginSignInRequest() : BeginSignInResultResponse
    suspend fun signInWithCredential(intent: Intent?): Result<String>
}