package com.mahezza.mahezza.data.repository

import android.util.Log
import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Course
import com.mahezza.mahezza.data.model.toCourse
import com.mahezza.mahezza.data.source.firebase.firestore.CourseFirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainCourseRepository @Inject constructor(
    private val courseFirebaseFirestore: CourseFirebaseFirestore
) : CourseRepository {
    override fun getCourseByPuzzleId(puzzleId : String): Flow<Result<Course>> {
        return courseFirebaseFirestore.getCourseByPuzzleId(puzzleId).map { firebaseResult ->
            if (!isFirebaseResultSuccess(firebaseResult)) return@map Result.Fail(firebaseResult.message)
            return@map Result.Success(
                data = firebaseResult.data!!.toCourse()
            )
        }
    }
}