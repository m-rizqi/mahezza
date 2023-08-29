package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.toChild
import com.mahezza.mahezza.data.model.toChildRequest
import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.firestore.ChildrenFirebaseFirestore
import com.mahezza.mahezza.data.source.firebase.response.ChildResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainChildrenRepository @Inject constructor(
    private val childrenFirebaseFirestore: ChildrenFirebaseFirestore
) : ChildrenRepository {
    override suspend fun insertChild(child: Child): Result<String> {
        val firebaseResult = childrenFirebaseFirestore.insertChild(child.toChildRequest())
        if (isFirebaseResultSuccess(firebaseResult)) return Result.Success(firebaseResult.data!!)
        return Result.Fail(firebaseResult.message)
    }

    override suspend fun getChildById(parentId: String, childId : String): Result<Child> {
        val firebaseResult = childrenFirebaseFirestore.getChildById(parentId, childId)
        if (isFirebaseResultSuccess(firebaseResult)) return Result.Success(firebaseResult.data!!.toChild())
        return Result.Fail(firebaseResult.message)
    }

    override fun getAllChild(parentId: String): Flow<Result<List<Child>>> {
        return childrenFirebaseFirestore.getAllChild(parentId).map {firebaseResult ->
            if (isFirebaseResultSuccess(firebaseResult)) return@map Result.Success(firebaseResult.data!!.map { childResponse -> childResponse.toChild() })
            else Result.Fail(firebaseResult.message)
        }
    }

}