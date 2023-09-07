package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {

    fun getCourseByPuzzleId(puzzleId : String): Flow<Result<Course>>
}