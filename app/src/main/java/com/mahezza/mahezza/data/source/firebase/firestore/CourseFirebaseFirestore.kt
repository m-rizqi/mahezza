package com.mahezza.mahezza.data.source.firebase.firestore

import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.response.CourseResponse
import kotlinx.coroutines.flow.Flow

interface CourseFirebaseFirestore {
    companion object {
        const val COURSE_PATH = "courses"
        const val PUZZLE_ID_FIELD = "puzzleId"
    }

    fun getCourseByPuzzleId(puzzleId : String) : Flow<FirebaseResult<out CourseResponse>>
}