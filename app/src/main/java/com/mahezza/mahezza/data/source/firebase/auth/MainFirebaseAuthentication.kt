package com.mahezza.mahezza.data.source.firebase.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.source.firebase.request.EmailAndPasswordRequest
import com.mahezza.mahezza.data.source.firebase.response.BeginSignInResultResponse
import com.mahezza.mahezza.data.source.firebase.response.SignInRegisterResponse
import com.mahezza.mahezza.di.FirebaseModule
import com.mahezza.mahezza.di.IODispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainFirebaseAuthentication @Inject constructor(
    @FirebaseModule.FirebaseWebClientId
    private val firebaseWebClientId : String,
    @ApplicationContext
    private val context: Context,
    @IODispatcher
    private val dispatcher: CoroutineDispatcher
) : FirebaseAuthentication {
    private val firebaseAuth = Firebase.auth
    private val oneTapClient = Identity.getSignInClient(context)

    override fun isLogin(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun isAnonymous() : Boolean {
        return if (!isLogin()) true else firebaseAuth.currentUser?.isAnonymous ?: true
    }

    override suspend fun beginSignInRequest(): BeginSignInResultResponse {
        return withContext(Dispatchers.IO){
            val signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build())
                .setGoogleIdTokenRequestOptions(GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(firebaseWebClientId)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
                )
                .build()
            try {
                val beginSignInResult = oneTapClient.beginSignIn(signInRequest).await()
                return@withContext BeginSignInResultResponse(
                    true, beginSignInResult.pendingIntent.intentSender, beginSignInResult, null
                )
            }catch (e : Exception){
                return@withContext BeginSignInResultResponse(
                    false, null, null, StringResource.DynamicString(e.message.toString())
                )
            }
        }
    }

    override suspend fun signInWithCredential(data : Intent?): SignInRegisterResponse {
        return withContext(dispatcher){
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            val googleIdToken = credential.googleIdToken ?: return@withContext SignInRegisterResponse(false, null, StringResource.DynamicString("Id token not passed"))
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            try {
                val authResult = firebaseAuth.signInWithCredential(firebaseCredential).await()
                return@withContext SignInRegisterResponse(true, authResult.user, null)
            }catch (e : Exception){
                return@withContext SignInRegisterResponse(false, null, StringResource.DynamicString(e.message.toString()))
            }
        }
    }

    override suspend fun signInWithEmailAndPassword(emailAndPasswordRequest: EmailAndPasswordRequest): SignInRegisterResponse {
        return withContext(dispatcher){
            try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(emailAndPasswordRequest.email, emailAndPasswordRequest.password).await()
                return@withContext SignInRegisterResponse(true, authResult.user, null)
            }catch (e : Exception){
                return@withContext SignInRegisterResponse(false, null, StringResource.DynamicString(e.message.toString()))
            }
        }
    }

    override suspend fun registerWithEmailAndPassword(emailAndPasswordRequest: EmailAndPasswordRequest) : SignInRegisterResponse {
        return withContext(dispatcher){
            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(emailAndPasswordRequest.email, emailAndPasswordRequest.password).await()
                return@withContext SignInRegisterResponse(true, authResult.user, null)
            }catch (e : Exception){
                return@withContext SignInRegisterResponse(false, null, StringResource.DynamicString(e.message.toString()))
            }
        }
    }

    override suspend fun signInAnonymously() : SignInRegisterResponse {
        return withContext(dispatcher){
            try {
                val authResult = firebaseAuth.signInAnonymously().await()
                return@withContext SignInRegisterResponse(true, authResult.user, null)
            }catch (e : Exception){
                return@withContext SignInRegisterResponse(false, null, StringResource.DynamicString(e.message.toString()))
            }
        }
    }

    override fun signOut() = firebaseAuth.signOut()

    override fun getCurrentUser() = firebaseAuth.currentUser
}