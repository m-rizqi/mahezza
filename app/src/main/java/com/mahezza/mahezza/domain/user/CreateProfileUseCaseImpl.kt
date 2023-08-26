package com.mahezza.mahezza.domain.user

import android.content.ContentResolver
import android.graphics.Bitmap
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.common.loadBitmapFromUri
import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.repository.UserRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.data.source.firebase.storage.FirebaseStorage
import com.mahezza.mahezza.data.source.firebase.storage.FirebaseStorage.Companion.USER_PATH
import com.mahezza.mahezza.data.source.firebase.storage.ImageRequest
import com.mahezza.mahezza.di.IODispatcher
import com.mahezza.mahezza.domain.KeyValue
import com.mahezza.mahezza.domain.ResultWithKeyMessage
import com.mahezza.mahezza.domain.ResultWithKeyMessages
import com.mahezza.mahezza.domain.anyOfValidatesIsFail
import com.mahezza.mahezza.domain.gatherErrorFromValidates
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateProfileUseCaseImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val userRepository: UserRepository,
    private val dataStore: MahezzaDataStore,
    @IODispatcher
    private val dispatcher: CoroutineDispatcher,
    private val contentResolver: ContentResolver
) : CreateProfileUseCase {
    override suspend fun invoke(createProfileData: CreateProfileUseCase.CreateProfileData): ResultWithKeyMessages<String> {

        if (createProfileData.id.isBlank()) createProfileData.id = dataStore.firebaseUserIdPreference.first() ?: return ResultWithKeyMessages.Fail(
            listOf(KeyValue("general", StringResource.StringResWithParams(R.string.user_id_is_not_found)))
        )

        val nameValidity = validateName(createProfileData.name)
        val jobValidity = validateJob(createProfileData.job)
        val relationshipWithChildrenValidity = validateRelationshipWithChildren(createProfileData.relationshipWithChildren)
        val timeSpendWithChildrenValidity = validateTimeSpendWithChildren(createProfileData.timeSpendWithChildren)

        val errorMessages = gatherErrorFromValidates(nameValidity, jobValidity, relationshipWithChildrenValidity, timeSpendWithChildrenValidity)
        if (anyOfValidatesIsFail(nameValidity, jobValidity, relationshipWithChildrenValidity, timeSpendWithChildrenValidity)){
            return ResultWithKeyMessages.Fail(errorMessages)
        }
        val photoUrl = saveAndGetUpdatedPhotoUrl(createProfileData)
        val result = userRepository.insertUser(createProfileData.toUser(photoUrl))
        return when(result){
            is Result.Fail -> ResultWithKeyMessages.Fail(
                listOf(KeyValue("general", result.message))
            )

            is Result.Success -> {
                ResultWithKeyMessages.Success(result.data ?: createProfileData.id)
            }
        }
    }

    private suspend fun saveAndGetUpdatedPhotoUrl(createProfileData: CreateProfileUseCase.CreateProfileData): String {
        if (createProfileData.newPhotoUri == null) return createProfileData.photoUrl ?: ""
        val bitmap = loadBitmapFromUri(contentResolver, createProfileData.newPhotoUri, dispatcher)
        return saveImageToFirebaseStorage(createProfileData.id, bitmap)
    }

    private suspend fun saveImageToFirebaseStorage(id : String, bitmap: Bitmap?): String {
        val imageRequest = ImageRequest(
            id = id,
            bitmap = bitmap
        )
        val firebaseResult = firebaseStorage.insertOrUpdateImage(USER_PATH, imageRequest)
        return firebaseResult.data ?: ""
    }

    private fun validateName(name : KeyValue<String>): ResultWithKeyMessage<Boolean> {
        return if (name.value.isBlank()) ResultWithKeyMessage.Fail(KeyValue(name.key, StringResource.StringResWithParams(R.string.name_cant_empty)))
        else ResultWithKeyMessage.Success(true)
    }

    private fun validateJob(job : KeyValue<String>): ResultWithKeyMessage<Boolean> {
        return if (job.value.isBlank()) ResultWithKeyMessage.Fail(KeyValue(job.key, StringResource.StringResWithParams(R.string.job_cant_empty)))
        else ResultWithKeyMessage.Success(true)
    }

    private fun validateRelationshipWithChildren(relationshipWithChildren : KeyValue<String>): ResultWithKeyMessage<Boolean> {
        return if (relationshipWithChildren.value.isBlank()) ResultWithKeyMessage.Fail(KeyValue(relationshipWithChildren.key, StringResource.StringResWithParams(R.string.relationship_with_children_cant_empty)))
        else ResultWithKeyMessage.Success(true)
    }

    private fun validateTimeSpendWithChildren(timeSpendWithChildren : KeyValue<String>): ResultWithKeyMessage<Boolean> {
        return if (timeSpendWithChildren.value.isBlank()) ResultWithKeyMessage.Fail(KeyValue(timeSpendWithChildren.key, StringResource.StringResWithParams(R.string.time_spend_with_children_cant_empty)))
        else ResultWithKeyMessage.Success(true)
    }
}