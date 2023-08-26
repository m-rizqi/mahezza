package com.mahezza.mahezza.data.source.firebase.firestore

import android.content.res.Resources
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.firestore.PuzzleFirebaseFirestore.Companion.PUZZLE_PATH
import com.mahezza.mahezza.data.source.firebase.firestore.PuzzleFirebaseFirestore.Companion.QR_CODE_PATH
import com.mahezza.mahezza.data.source.firebase.response.PuzzleResponse
import com.mahezza.mahezza.data.source.firebase.response.QRCodeResponse
import com.mahezza.mahezza.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainPuzzleFirebaseFirestore @Inject constructor(
    @IODispatcher
    private val dispatcher: CoroutineDispatcher
) : PuzzleFirebaseFirestore{

    private val firestore = Firebase.firestore
    private val puzzleReference = firestore.collection(PUZZLE_PATH)
    private val qrCodeReference = firestore.collection(QR_CODE_PATH)

    override suspend fun findPuzzleIdByQRCode(qrcode: String): FirebaseResult<PuzzleResponse> {
        return withContext(dispatcher) {
            try {
                val snapshot = qrCodeReference.document(qrcode).get().await()
                val qrCodeResponse: QRCodeResponse = snapshot.toObject(QRCodeResponse::class.java)
                    ?: throw Resources.NotFoundException("Puzzle with code $qrcode not found!")

                return@withContext getPuzzleById(qrCodeResponse.puzzleId)
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

    override suspend fun getPuzzleById(id: String): FirebaseResult<PuzzleResponse> {
        return withContext(dispatcher){
            try {
                val snapshot = puzzleReference.document(id).get().await()
                val puzzleResponse : PuzzleResponse = snapshot.toObject(PuzzleResponse::class.java)
                    ?: throw Resources.NotFoundException("Puzzle with id $id not found!")
                return@withContext FirebaseResult(puzzleResponse, true, null)
            }catch (e : FirebaseFirestoreException){
                return@withContext FirebaseResult(null, false, StringResource.DynamicString(e.message.toString()))
            }
            catch (e : Resources.NotFoundException){
                return@withContext FirebaseResult(null, false, StringResource.DynamicString(e.message.toString()))
            }
        }
    }


}