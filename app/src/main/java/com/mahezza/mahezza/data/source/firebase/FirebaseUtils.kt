package com.mahezza.mahezza.data.source.firebase

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

fun <T> CollectionReference.addSnapshotListenerFlow(
    dataType: Class<T>,
    dispatcher: CoroutineDispatcher,
    notFoundOrEmptyCollectionMessage: StringResource? = null
): Flow<FirebaseResult<out List<T>>> = callbackFlow {
    val listener = object : EventListener<QuerySnapshot> {
        override fun onEvent(snapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
            if (error != null) {
                trySend(
                    FirebaseResult(
                        data = null,
                        isSuccess = false,
                        message = error.message?.let {
                            StringResource.DynamicString(it)
                        } ?: StringResource.StringResWithParams(R.string.problem_occur_try_again)
                    )
                )
                cancel()
                return
            }
            if (isCollectionExistAndNotEmpty(snapshot)){
                val results = mutableListOf<T>()
                snapshot?.documents?.forEach {document ->
                    val convertedData = document.toObject(dataType)
                    if (convertedData != null){
                        results.add(convertedData)
                    }
                }
                trySend(FirebaseResult(results.toList(), true, null))
            } else {
                trySend(FirebaseResult(listOf(), true, notFoundOrEmptyCollectionMessage))
            }
        }
    }

    val registration = addSnapshotListener(listener)
    awaitClose { registration.remove() }
}.flowOn(dispatcher)

private fun isCollectionExistAndNotEmpty(snapshot: QuerySnapshot?): Boolean = snapshot != null && !snapshot.isEmpty

fun <T> DocumentReference.addSnapshotListenerFlow(
    dataType: Class<T>,
    dispatcher: CoroutineDispatcher,
    notFoundMessage: StringResource?
): Flow<FirebaseResult<T>> = callbackFlow<FirebaseResult<T>> {
    val listener = object : EventListener<DocumentSnapshot> {
        override fun onEvent(snapshot: DocumentSnapshot?, exception: FirebaseFirestoreException?) {
            if (exception != null) {
                trySend(
                    FirebaseResult(
                        data = null,
                        isSuccess = false,
                        message = exception.message?.let {
                            StringResource.DynamicString(it)
                        } ?: StringResource.StringResWithParams(R.string.problem_occur_try_again)
                    )
                )
                cancel()
                return
            }

            if (isDocumentExist(snapshot)) {
                val data = snapshot?.toObject(dataType)
                trySend(FirebaseResult(data, true, null))
            } else {
                trySend(FirebaseResult(null, false, notFoundMessage))
            }
        }
    }

    val registration = addSnapshotListener(listener)
    awaitClose { registration.remove() }
}.flowOn(dispatcher)

private fun isDocumentExist(snapshot: DocumentSnapshot?): Boolean = snapshot != null && snapshot.exists()

fun <T> Query.addSnapshotListenerFlow(
    dataType: Class<T>,
    dispatcher: CoroutineDispatcher,
    notFoundOrEmptyCollectionMessage: StringResource? = null
): Flow<FirebaseResult<out List<T>>> = callbackFlow {
    val listener = object : EventListener<QuerySnapshot> {
        override fun onEvent(snapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
            if (error != null) {
                trySend(
                    FirebaseResult(
                        data = null,
                        isSuccess = false,
                        message = error.message?.let {
                            StringResource.DynamicString(it)
                        } ?: StringResource.StringResWithParams(R.string.problem_occur_try_again)
                    )
                )
                cancel()
                return
            }
            if (isCollectionExistAndNotEmpty(snapshot)){
                val results = mutableListOf<T>()
                snapshot?.documents?.forEach {document ->
                    val convertedData = document.toObject(dataType)
                    if (convertedData != null){
                        results.add(convertedData)
                    }
                }

                trySend(FirebaseResult(results.toList(), true, null))
            } else {
                trySend(FirebaseResult(listOf(), true, notFoundOrEmptyCollectionMessage))
            }
        }
    }

    val registration = addSnapshotListener(listener)
    awaitClose { registration.remove() }
}.flowOn(dispatcher)

fun <T> Query.addTakeOneSnapshotListenerFlow(
    dataType: Class<T>,
    dispatcher: CoroutineDispatcher,
    notFoundOrEmptyCollectionMessage: StringResource? = null
): Flow<FirebaseResult<out T>> = callbackFlow {
    val listener = object : EventListener<QuerySnapshot> {
        override fun onEvent(snapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
            if (error != null) {
                trySend(
                    FirebaseResult(
                        data = null,
                        isSuccess = false,
                        message = error.message?.let {
                            StringResource.DynamicString(it)
                        } ?: StringResource.StringResWithParams(R.string.problem_occur_try_again)
                    )
                )
                cancel()
                return
            }
            if (isCollectionExistAndNotEmpty(snapshot)) {
                Timber.tag("asdasd").d(snapshot?.documents?.firstOrNull()?.data.toString())
                val convertedData = snapshot?.documents?.firstOrNull()?.let { document ->
                    document.toObject(dataType)
                }

                trySend(FirebaseResult(convertedData, true, null))
            } else {
                trySend(FirebaseResult(null, true, notFoundOrEmptyCollectionMessage))
            }
        }
    }

    val registration = addSnapshotListener(listener)
    awaitClose { registration.remove() }
}.flowOn(dispatcher)