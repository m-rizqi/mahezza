package com.mahezza.mahezza.data.source.firebase.firestore

import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.request.UserRequest

interface UserFirebaseFirestore {
    suspend fun insertUser(userRequest : UserRequest) : FirebaseResult<String>
}