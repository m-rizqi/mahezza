package com.mahezza.mahezza.data.source.firebase.auth

import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.mahezza.mahezza.data.source.firebase.request.EmailAndPasswordRequest
import com.mahezza.mahezza.data.source.firebase.response.BeginSignInResultResponse
import com.mahezza.mahezza.data.source.firebase.response.SignInRegisterResponse
import com.mahezza.mahezza.data.source.firebase.response.SignInResult

interface FirebaseAuthentication {
    fun isLogin() : Boolean
    fun isAnonymous() : Boolean
    suspend fun beginSignInRequest(): BeginSignInResultResponse
    suspend fun signInWithCredential(data : Intent?): SignInRegisterResponse
    suspend fun signInWithEmailAndPassword(emailAndPasswordRequest: EmailAndPasswordRequest): SignInRegisterResponse
    suspend fun registerWithEmailAndPassword(emailAndPasswordRequest: EmailAndPasswordRequest) : SignInRegisterResponse
    suspend fun signInAnonymously() : SignInRegisterResponse
    fun signOut()
    fun getCurrentUser() : FirebaseUser?
}