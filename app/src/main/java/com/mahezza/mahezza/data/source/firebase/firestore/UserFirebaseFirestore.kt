package com.mahezza.mahezza.data.source.firebase.firestore

import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.request.InsertRedeemedPuzzleRequest
import com.mahezza.mahezza.data.source.firebase.request.UserRequest
import com.mahezza.mahezza.data.source.firebase.response.UserResponse

interface UserFirebaseFirestore {
    companion object {
        const val USER_PATH = "users"
        const val PUZZLE_PATH = "puzzles"
    }
    suspend fun insertUser(userRequest : UserRequest) : FirebaseResult<String>
    suspend fun getUserById(id : String) : FirebaseResult<UserResponse>
    suspend fun insertRedeemedPuzzle(userId : String, insertRedeemedPuzzleRequest: InsertRedeemedPuzzleRequest) : FirebaseResult<Boolean>
}