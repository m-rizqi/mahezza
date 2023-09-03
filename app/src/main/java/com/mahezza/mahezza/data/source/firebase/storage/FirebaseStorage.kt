package com.mahezza.mahezza.data.source.firebase.storage

import com.mahezza.mahezza.data.source.firebase.FirebaseResult

interface FirebaseStorage {

    companion object {
        const val GAME_PATH = "games"
        const val USER_PATH = "users"
        const val CHILDREN_PATH = "children"
    }

    suspend fun insertOrUpdateImage(path : String, imageRequest: ImageRequest) : FirebaseResult<String>
    suspend fun insertOrUpdateAllImages(path : String, imageRequests: List<ImageRequest>) : List<FirebaseResult<String>>
    suspend fun getImage(path : String, id : String) : FirebaseResult<ImageResponse>
    suspend fun getAllImages(path : String) : List<FirebaseResult<ImageResponse>>
    suspend fun deleteItem(path: String, id: String)
    suspend fun deleteAllItems(path: String)
}