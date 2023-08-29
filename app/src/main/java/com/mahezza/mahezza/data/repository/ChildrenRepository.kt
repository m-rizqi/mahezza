package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.response.ChildResponse
import kotlinx.coroutines.flow.Flow

interface ChildrenRepository {
    suspend fun insertChild(child: Child) : Result<String>
    suspend fun getChildById(parentId : String, childId: String) : Result<Child>
    fun getAllChild(parentId: String) : Flow<Result<List<Child>>>
}