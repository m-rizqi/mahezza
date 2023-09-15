package com.mahezza.mahezza.data.source.firebase.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.Game
import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.addSnapshotListenerFlow
import com.mahezza.mahezza.data.source.firebase.firestore.GameFirebaseFirestore.Companion.GAME_PATH
import com.mahezza.mahezza.data.source.firebase.firestore.GameFirebaseFirestore.Companion.STATUS_FIELD
import com.mahezza.mahezza.data.source.firebase.request.GameRequest
import com.mahezza.mahezza.data.source.firebase.response.GameResponse
import com.mahezza.mahezza.data.source.firebase.response.LastGameActivityResponse
import com.mahezza.mahezza.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
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

    override fun getLastGameActivities(parentId: String): Flow<FirebaseResult<out List<LastGameActivityResponse>>> {
        if (parentId == "") return emptyFlow()
        val gameQuery = gameCollection(parentId).whereNotEqualTo(STATUS_FIELD, Game.Status.Finished.const)
        return gameQuery.addSnapshotListenerFlow(
            dataType = LastGameActivityResponse::class.java,
            dispatcher = dispatcher,
            notFoundOrEmptyCollectionMessage = StringResource.StringResWithParams(R.string.last_activity_not_found)
        )
    }

    override suspend fun getGame(parentId: String, gameId: String): FirebaseResult<GameResponse> {
        return withContext(dispatcher){
            try {
                val snapshot = gameDocumentReference(parentId, gameId)
                    .get().await()
                val gameResponse = snapshot.toObject(GameResponse::class.java)
                return@withContext FirebaseResult(gameResponse, true, null)
            } catch (e: FirebaseFirestoreException){
                return@withContext FirebaseResult(null, false, StringResource.DynamicString(e.message.toString()))
            }
        }
    }
}