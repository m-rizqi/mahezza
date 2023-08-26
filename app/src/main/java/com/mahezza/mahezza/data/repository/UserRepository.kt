package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.User
import com.mahezza.mahezza.data.source.firebase.request.InsertRedeemedPuzzleRequest

interface UserRepository {
    suspend fun insertUser(user: User) : Result<String>
    suspend fun getUserById(id: String) : Result<User>

    suspend fun insertRedeemedPuzzle(userId : String, insertRedeemedPuzzleRequest: InsertRedeemedPuzzleRequest) : Result<Boolean>
}