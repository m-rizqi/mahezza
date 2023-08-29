package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.User
import com.mahezza.mahezza.data.model.toChild
import com.mahezza.mahezza.data.model.toUser
import com.mahezza.mahezza.data.model.toUserRequest
import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.firestore.UserFirebaseFirestore
import com.mahezza.mahezza.data.source.firebase.request.InsertRedeemedPuzzleRequest
import com.mahezza.mahezza.data.source.firebase.response.RedeemedPuzzleResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override suspend fun insertRedeemedPuzzle(
        userId: String,
        insertRedeemedPuzzleRequest: InsertRedeemedPuzzleRequest
    ): Result<Boolean> {
        val firebaseResult = userFirebaseFirestore.insertRedeemedPuzzle(userId, insertRedeemedPuzzleRequest)
        if (isFirebaseResultSuccess(firebaseResult)) return Result.Success(firebaseResult.data!!)
        return Result.Fail(firebaseResult.message)
    }

    override fun getRedeemedPuzzles(userId: String): Flow<Result<List<RedeemedPuzzleResponse>>> {
        return userFirebaseFirestore.getRedeemedPuzzleIds(userId).map {firebaseResult ->
            if (isFirebaseResultSuccess(firebaseResult)) return@map Result.Success(firebaseResult.data!!)
            else Result.Fail(firebaseResult.message)
        }
    }

}