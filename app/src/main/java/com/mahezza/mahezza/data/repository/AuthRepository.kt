package com.mahezza.mahezza.data.repository

import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.source.firebase.response.BeginSignInResultResponse

interface AuthRepository {
    suspend fun registerWithEmailAndPassword(email: String, password: String): Result<FirebaseUser>
    suspend fun loginWithEmailAndPassword(email: String, password: String): Result<FirebaseUser>
    suspend fun beginSignInRequest(): BeginSignInResultResponse
    suspend fun signInWithCredential(intent: Intent?): Result<FirebaseUser>
    suspend fun sendPasswordResetEmail(email: String): Result<Boolean>
    fun logOut() : Result<Boolean>
}