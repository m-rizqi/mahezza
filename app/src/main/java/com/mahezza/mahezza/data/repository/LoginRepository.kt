package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.source.firebase.response.SignInRegisterResponse

interface LoginRepository {
    fun isLogin() : Boolean
    fun signOut() : Boolean
    fun loginWithEmailAndPassword(email : String, password : String) : Result<SignInRegisterResponse>
}