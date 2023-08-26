package com.mahezza.mahezza.domain.children

import android.content.ContentResolver
import android.graphics.Bitmap
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.common.loadBitmapFromUri
import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.repository.ChildrenRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.data.source.firebase.storage.FirebaseStorage
import com.mahezza.mahezza.data.source.firebase.storage.ImageRequest
import com.mahezza.mahezza.di.IODispatcher
import com.mahezza.mahezza.domain.KeyValue
import com.mahezza.mahezza.domain.ResultWithKeyMessage
import com.mahezza.mahezza.domain.ResultWithKeyMessages
import com.mahezza.mahezza.domain.anyOfValidatesIsFail
import com.mahezza.mahezza.domain.common.FormatDateUseCase
import com.mahezza.mahezza.domain.gatherErrorFromValidates
import com.mahezza.mahezza.domain.user.CreateProfileUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

class InsertChildUseCaseImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val childrenRepository: ChildrenRepository,
    private val dataStore: MahezzaDataStore,
    @IODispatcher
    private val dispatcher: CoroutineDispatcher,
    private val contentResolver: ContentResolver
) : InsertChildUseCase {

    private val formatDateUseCase = FormatDateUseCase()

    override suspend fun invoke(insertChildData: InsertChildUseCase.InsertChildData): ResultWithKeyMessages<String> {
        insertChildData.parentId = dataStore.firebaseUserIdPreference.first() ?: return ResultWithKeyMessages.Fail(
            listOf(KeyValue("general", StringResource.StringResWithParams(R.string.user_id_is_not_found)))
        )
        insertChildData.id = UUID.randomUUID().toString()

        val nameValidity = validateName(insertChildData.name)

        val errorMessages = gatherErrorFromValidates(nameValidity)
        if (anyOfValidatesIsFail(nameValidity)){
            return ResultWithKeyMessages.Fail(errorMessages)
        }

        val photoUrl = saveAndGetUpdatedPhotoUrl(insertChildData)
        val child = insertChildData.toChild(photoUrl)
        val result = childrenRepository.insertChild(child)
        return when(result){
            is Result.Fail -> ResultWithKeyMessages.Fail(
                listOf(KeyValue("general", result.message))
            )

            is Result.Success -> {
                ResultWithKeyMessages.Success(photoUrl)
            }
        }
    }

    private suspend fun saveAndGetUpdatedPhotoUrl(insertChildData: InsertChildUseCase.InsertChildData): String {
        if (insertChildData.photoUri == null) return ""
        val bitmap = loadBitmapFromUri(contentResolver, insertChildData.photoUri, dispatcher)
        return saveImageToFirebaseStorage(insertChildData.parentId, insertChildData.id, bitmap)
    }

    private suspend fun saveImageToFirebaseStorage(parentId: String, childId: String, bitmap: Bitmap?): String {
        val imageRequest = ImageRequest(
            id = childId,
            bitmap = bitmap
        )
        val firebaseResult = firebaseStorage.insertOrUpdateImage("${FirebaseStorage.USER_PATH}/${FirebaseStorage.CHILDREN_PATH}/$parentId", imageRequest)
        return firebaseResult.data ?: ""
    }

    private fun validateName(name : KeyValue<String>): ResultWithKeyMessage<Boolean> {
        return if (name.value.isBlank()) ResultWithKeyMessage.Fail(KeyValue(name.key, StringResource.StringResWithParams(R.string.name_cant_empty)))
        else ResultWithKeyMessage.Success(true)
    }
}