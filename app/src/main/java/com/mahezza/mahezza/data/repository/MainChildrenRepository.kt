package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.toChild
import com.mahezza.mahezza.data.model.toChildRequest
import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.firestore.ChildrenFirebaseFirestore
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

    private fun isFirebaseResultSuccess(firebaseResult: FirebaseResult<out Any>): Boolean = firebaseResult.isSuccess && firebaseResult.data != null
}