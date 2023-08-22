package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.User

interface UserRepository {
    suspend fun insertUser(user: User) : Result<String>
}