package com.mahezza.mahezza.data.source.firebase.firestore

import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.request.ChildRequest
import com.mahezza.mahezza.data.source.firebase.response.ChildResponse

interface ChildrenFirebaseFirestore {
    companion object {
        const val CHILDREN_PATH = "children"
    }

    suspend fun insertChild(childRequest: ChildRequest) : FirebaseResult<String>
    suspend fun getChildById(parentId : String, childId : String) : FirebaseResult<ChildResponse>
}