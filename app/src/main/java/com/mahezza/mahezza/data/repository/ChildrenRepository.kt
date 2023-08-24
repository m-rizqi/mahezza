package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Child

interface ChildrenRepository {
    suspend fun insertChild(child: Child) : Result<String>
    suspend fun getChildById(parentId : String, childId: String) : Result<Child>
}