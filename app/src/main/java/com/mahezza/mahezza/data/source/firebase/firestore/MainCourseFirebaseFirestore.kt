package com.mahezza.mahezza.data.source.firebase.firestore

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.addSnapshotListenerFlow
import com.mahezza.mahezza.data.source.firebase.addTakeOneSnapshotListenerFlow
import com.mahezza.mahezza.data.source.firebase.firestore.CourseFirebaseFirestore.Companion.COURSE_PATH
import com.mahezza.mahezza.data.source.firebase.firestore.CourseFirebaseFirestore.Companion.PUZZLE_ID_FIELD
import com.mahezza.mahezza.data.source.firebase.response.CourseResponse
import com.mahezza.mahezza.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainCourseFirebaseFirestore @Inject constructor(
    @IODispatcher
    private val dispatcher: CoroutineDispatcher
) : CourseFirebaseFirestore {
    private val firestore = Firebase.firestore
    private val coursesCollection = firestore.collection(COURSE_PATH)
    private val courseQuery : (puzzleId : String) -> Query = {puzzleId ->
        coursesCollection.whereEqualTo(PUZZLE_ID_FIELD, puzzleId)
    }

    override fun getCourseByPuzzleId(puzzleId: String): Flow<FirebaseResult<out CourseResponse>> {
        val courseQuery = courseQuery(puzzleId)
        return courseQuery.addTakeOneSnapshotListenerFlow(
            dataType = CourseResponse::class.java,
            dispatcher = dispatcher,
            notFoundOrEmptyCollectionMessage = StringResource.StringResWithParams(R.string.course_not_found)
        )
    }
}