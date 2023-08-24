package com.mahezza.mahezza.data.source.firebase.storage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.random.Random

class MainFirebaseStorage @Inject constructor(
    @IODispatcher
    private val dispatcher: CoroutineDispatcher
) : FirebaseStorage{

    private val firebaseStorage = Firebase.storage

    override suspend fun insertOrUpdateImage(path: String, imageRequest: ImageRequest): FirebaseResult<String> {
        return withContext(dispatcher){
            val reference = getReferences(path).child(imageRequest.id)
            try {
                reference.putBytes(bitmapToByteArray(imageRequest.bitmap)).await()
                val downloadUrl = reference.downloadUrl.await().toString()
                return@withContext FirebaseResult(downloadUrl, true, null)
            }catch (e : StorageException){
                return@withContext FirebaseResult(null, false, StringResource.DynamicString(e.message.toString()))
            }
        }
    }

    override suspend fun insertOrUpdateAllImages(
        path: String,
        imageRequests: List<ImageRequest>
    ): List<FirebaseResult<String>> {
        return withContext(dispatcher){
            val firebaseResults = mutableListOf<FirebaseResult<String>>()
            imageRequests.forEach { imageRequest ->
                val result = insertOrUpdateImage(path, imageRequest)
                firebaseResults.add(result)
            }
            return@withContext firebaseResults
        }
    }

    override suspend fun getImage(
        path: String,
        id: String
    ): FirebaseResult<ImageResponse> {
        return withContext(dispatcher){
            val reference = getReferences(path).child(id)
            try {
                val byteArray = reference.getBytes(Long.MAX_VALUE).await()
                val bitmap = byteArrayToBitmap(byteArray)
                return@withContext FirebaseResult(ImageResponse(id, bitmap), true, null)
            }catch (e : StorageException){
                return@withContext FirebaseResult(null, false, StringResource.DynamicString(e.message.toString()))
            }
        }
    }

    override suspend fun getAllImages(path: String): List<FirebaseResult<ImageResponse>> {
        return withContext(dispatcher){
            val firebaseResults = mutableListOf<FirebaseResult<ImageResponse>>()
            val reference = getReferences(path)
            try {
                val listResult = reference.listAll().await()
                listResult.items.forEach { storageReference ->
                    try {
                        val byteArray = storageReference.getBytes(Long.MAX_VALUE).await()
                        val bitmap = byteArrayToBitmap(byteArray)
                        firebaseResults.add(
                            FirebaseResult(
                                ImageResponse(
                                    storageReference.name,
                                    bitmap
                                ),
                                true,
                                null
                            )
                        )
                    }catch (e : StorageException){
                        firebaseResults.add(FirebaseResult(null, false, StringResource.DynamicString(e.message.toString())))
                    }
                }
                return@withContext firebaseResults
            }catch (e : StorageException){
                return@withContext firebaseResults
            }
        }
    }

    override suspend fun deleteItem(path: String, id: String) {
        return withContext(dispatcher){
            val reference = getReferences(path).child(id)
            reference.delete()
        }
    }

    override suspend fun deleteAllItems(path: String) {
        return withContext(dispatcher){
            val reference = getReferences(path)
            reference.delete()
        }
    }

    private fun getReferences(path: String) = firebaseStorage.reference.child("${path}/")

    private fun bitmapToByteArray(bitmap: Bitmap?) : ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }

    private fun byteArrayToBitmap(byteArray: ByteArray) : Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}