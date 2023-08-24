package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.User
import com.mahezza.mahezza.data.model.toUser
import com.mahezza.mahezza.data.model.toUserRequest
import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.firestore.UserFirebaseFirestore
import javax.inject.Inject

class MainUserRepository @Inject constructor(
    private val userFirebaseFirestore: UserFirebaseFirestore
) : UserRepository {
    override suspend fun insertUser(user: User): Result<String> {
        val firebaseResult = userFirebaseFirestore.insertUser(
            user.toUserRequest()
        )
        if (isFirebaseResultSuccess(firebaseResult)) return Result.Success(firebaseResult.data!!)
        return Result.Fail(firebaseResult.message)
    }

    override suspend fun getUserById(id: String): Result<User> {
        val firebaseResult = userFirebaseFirestore.getUserById(id)
        if (isFirebaseResultSuccess(firebaseResult)) return Result.Success(firebaseResult.data!!.toUser())
        return Result.Fail(firebaseResult.message)
    }

    private fun isFirebaseResultSuccess(firebaseResult: FirebaseResult<out Any>): Boolean = firebaseResult.isSuccess && firebaseResult.data != null
}