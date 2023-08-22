package com.mahezza.mahezza.data.repository

import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.mahezza.mahezza.data.source.firebase.auth.FirebaseAuthentication
import com.mahezza.mahezza.data.source.firebase.request.EmailAndPasswordRequest
import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.source.firebase.response.BeginSignInResultResponse
import com.mahezza.mahezza.data.source.firebase.response.SignInRegisterResponse
import javax.inject.Inject

class MainAuthRepository @Inject constructor(
    private val firebaseAuthentication: FirebaseAuthentication
) : AuthRepository {
    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        val emailAndPasswordRequest = EmailAndPasswordRequest(email, password)
        val response = firebaseAuthentication.registerWithEmailAndPassword(emailAndPasswordRequest)
        return if (isRegisterSuccess(response)) {
            Result.Success(response.firebaseUser!!)
        } else {
            Result.Fail(response.message)
        }
    }

    override suspend fun beginSignInRequest(): BeginSignInResultResponse {
        return firebaseAuthentication.beginSignInRequest()
    }

    override suspend fun signInWithCredential(intent: Intent?): Result<FirebaseUser> {
        val response = firebaseAuthentication.signInWithCredential(intent)
        return if (isRegisterSuccess(response)) {
            Result.Success(response.firebaseUser!!)
        } else {
            Result.Fail(response.message)
        }
    }

    private fun isRegisterSuccess(response: SignInRegisterResponse): Boolean = response.isSuccess && response.firebaseUser != null
}