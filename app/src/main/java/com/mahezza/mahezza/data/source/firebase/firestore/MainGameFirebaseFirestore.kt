package com.mahezza.mahezza.data.source.firebase.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.firestore.GameFirebaseFirestore.Companion.GAME_PATH
import com.mahezza.mahezza.data.source.firebase.request.GameRequest
import com.mahezza.mahezza.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainGameFirebaseFirestore @Inject constructor(
    @IODispatcher
    val dispatcher: CoroutineDispatcher
) : GameFirebaseFirestore {
    private val firestore = Firebase.firestore
    private val usersCollection = firestore.collection(UserFirebaseFirestore.USER_PATH)
    private val gameCollection : (parentId : String) -> CollectionReference = {parentId ->
        usersCollection.document(parentId).collection(GAME_PATH)
    }
    private val gameDocumentReference : (parentId : String, gameId : String) -> DocumentReference = {parentId, gameId ->
        gameCollection(parentId).document(gameId)
    }

    override suspend fun saveGame(gameRequest: GameRequest): FirebaseResult<String> {
        return withContext(dispatcher){
            try {
                gameDocumentReference(gameRequest.parentId, gameRequest.id)
                    .set(gameRequest).await()
                return@withContext FirebaseResult(gameRequest.id, true, null)
            } catch (e: FirebaseFirestoreException){
                return@withContext FirebaseResult(null, false, StringResource.DynamicString(e.message.toString()))
            }
        }
    }
}