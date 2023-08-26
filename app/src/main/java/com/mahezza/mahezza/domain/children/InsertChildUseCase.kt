package com.mahezza.mahezza.domain.children

import android.net.Uri
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.domain.KeyValue
import com.mahezza.mahezza.domain.ResultWithKeyMessages

interface InsertChildUseCase {
    suspend operator fun invoke(insertChildData: InsertChildData) : ResultWithKeyMessages<String>

    data class InsertChildData(
        var parentId : String,
        var id : String,
        val name : KeyValue<String>,
        val gender : String,
        val birthdate : String,
        val photoUri : Uri?
    ){
        fun toChild(photoUrl : String = "") : Child{
            return Child(
                parentId = this.parentId,
                id = this.id,
                name = this.name.value,
                gender = this.gender,
                birthdate = this.birthdate,
                photoUrl = photoUrl
            )
        }
    }
}