package com.mahezza.mahezza.domain.user

import android.net.Uri
import com.mahezza.mahezza.data.model.User
import com.mahezza.mahezza.data.model.toUser
import com.mahezza.mahezza.domain.KeyValue
import com.mahezza.mahezza.domain.ResultWithKeyMessages

interface CreateProfileUseCase {
    suspend fun invoke(createProfileData: CreateProfileData) : ResultWithKeyMessages<String>

    data class CreateProfileData(
        var id : String,
        val name : KeyValue<String>,
        val job : KeyValue<String>,
        val relationshipWithChildren : KeyValue<String>,
        val timeSpendWithChildren : KeyValue<String>,
        val newPhotoUri : Uri? = null,
        val photoUrl : String? = null
    ) {
        fun toUser(photoUrl: String = "") : User{
            return User(
                id = this.id,
                name = this.name.value,
                photoUrl = photoUrl,
                job = this.job.value,
                relationshipWithChildren = this.relationshipWithChildren.value,
                timeSpendWithChildren = this.timeSpendWithChildren.value
            )
        }
    }
}