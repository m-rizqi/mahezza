package com.mahezza.mahezza.data.source.firebase.firestore

import android.content.res.Resources
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.addSnapshotListenerFlow
import com.mahezza.mahezza.data.source.firebase.firestore.ChildrenFirebaseFirestore.Companion.CHILDREN_PATH
import com.mahezza.mahezza.data.source.firebase.firestore.UserFirebaseFirestore.Companion.USER_PATH
import com.mahezza.mahezza.data.source.firebase.request.ChildRequest
import com.mahezza.mahezza.data.source.firebase.response.ChildResponse
import com.mahezza.mahezza.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainChildrenFirebaseFirestore(
    @IODispatcher
    private val dispatcher: CoroutineDispatcher
) : ChildrenFirebaseFirestore {
    private val firestore = Firebase.firestore
    private val parentsCollections = firestore.collection(USER_PATH)

    override suspend fun insertChild(childRequest: ChildRequest): FirebaseResult<String> {
           return withContext(dispatcher) {
               try {
                   getChildReference(childRequest.parentId, childRequest.id)
                       .set(childRequest).await()
                   return@withContext FirebaseResult(childRequest.id, true, null)
               } catch (e: FirebaseFirestoreException) {
                   return@withContext FirebaseResult(
                       null,
                       false,
                       StringResource.DynamicString(e.message.toString())
                   )
               }
           }
    }

    override suspend fun getChildById(parentId: String, childId: String): FirebaseResult<ChildResponse> {
        return withContext(dispatcher) {
            try {
                val snapshot = getChildReference(parentId, childId).get().await()
                val childResponse: ChildResponse = snapshot.toObject(ChildResponse::class.java)
                    ?: throw Resources.NotFoundException("Child with id $childId not found!")
                return@withContext FirebaseResult(childResponse, true, null)
            } catch (e: FirebaseFirestoreException) {
                return@withContext FirebaseResult(
                    null,
                    false,
                    StringResource.DynamicString(e.message.toString())
                )
            } catch (e: Resources.NotFoundException) {
                return@withContext FirebaseResult(
                    null,
                    false,
                    StringResource.DynamicString(e.message.toString())
                )
            }
        }
    }

    override fun getAllChild(parentId: String): Flow<FirebaseResult<out List<ChildResponse>>> {
        val childrenReference = getAllChildReference(parentId)
        return childrenReference.addSnapshotListenerFlow(
            dataType = ChildResponse::class.java,
            dispatcher = dispatcher,
            notFoundOrEmptyCollectionMessage = StringResource.StringResWithParams(R.string.children_not_found)
        )
    }

    private fun getChildReference(parentId : String, childId : String): DocumentReference {
        return parentsCollections.document(parentId).collection(CHILDREN_PATH).document(childId)
    }

    private fun getAllChildReference(parentId: String): CollectionReference {
        return parentsCollections.document(parentId).collection(CHILDREN_PATH)
    }

}