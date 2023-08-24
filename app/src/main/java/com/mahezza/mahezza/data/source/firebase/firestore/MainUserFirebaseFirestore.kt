package com.mahezza.mahezza.data.source.firebase.firestore

import android.content.res.Resources.NotFoundException
import com.google.firebase.database.DatabaseException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.firestore.UserFirebaseFirestore.Companion.USER_PATH
import com.mahezza.mahezza.data.source.firebase.request.UserRequest
import com.mahezza.mahezza.data.source.firebase.response.UserResponse
import com.mahezza.mahezza.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class MainUserFirebaseFirestore(
    @IODispatcher
    private val dispatcher: CoroutineDispatcher
) : UserFirebaseFirestore{
    private val firestore = Firebase.firestore
    private val usersCollection = firestore.collection(USER_PATH)

    override suspend fun insertUser(userRequest: UserRequest): FirebaseResult<String> {
           return withContext(dispatcher){
               try {
                   usersCollection.document(userRequest.id)
                       .set(userRequest).await()
                   return@withContext FirebaseResult(userRequest.id, true, null)
               } catch (e: FirebaseFirestoreException){
                   return@withContext FirebaseResult(null, false, StringResource.DynamicString(e.message.toString()))
               }
           }
    }

    override suspend fun getUserById(id: String): FirebaseResult<UserResponse> {
        return withContext(dispatcher){
            try {
                val snapshot = usersCollection.document(id).get().await()
                val userResponse : UserResponse = snapshot.toObject(UserResponse::class.java)
                    ?: throw NotFoundException("User with id $id not found!")
                return@withContext FirebaseResult(userResponse, true, null)
            }catch (e : FirebaseFirestoreException){
                return@withContext FirebaseResult(null, false, StringResource.DynamicString(e.message.toString()))
            }
            catch (e : NotFoundException){
                return@withContext FirebaseResult(null, false, StringResource.DynamicString(e.message.toString()))
            }
        }
    }

}