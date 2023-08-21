package com.mahezza.mahezza.data.source.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.request.UserRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val USER_PATH = "users"

class MainUserFirebaseFirestore(
    private val dispatcher: CoroutineDispatcher
) : UserFirebaseFirestore{
    private val firestore = Firebase.firestore

    override suspend fun insertUser(userRequest: UserRequest): FirebaseResult<String> {
           return withContext(dispatcher){
               try {
                   firestore.collection(USER_PATH).document(userRequest.id)
                       .set(userRequest).await()
                   return@withContext FirebaseResult(userRequest.id, true, null)
               } catch (e: FirebaseFirestoreException){
                   return@withContext FirebaseResult(null, false, StringResource.DynamicString(e.message.toString()))
               }
           }
    }

}