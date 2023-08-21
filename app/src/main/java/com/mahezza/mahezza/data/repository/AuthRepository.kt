package com.mahezza.mahezza.data.repository

import com.google.firebase.auth.FirebaseUser
import com.mahezza.mahezza.data.Result

interface AuthRepository {
    suspend fun registerWithEmailAndPassword(email: String, password: String): Result<FirebaseUser>
}