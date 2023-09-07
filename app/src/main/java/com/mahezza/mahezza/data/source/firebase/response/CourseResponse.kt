package com.mahezza.mahezza.data.source.firebase.response

data class CourseResponse(
    val name : String = "",
    val courseId : String = "",
    val puzzleId : String = "",
    val subCourses : List<SubCourseResponse> = emptyList(),
    val banner : String = ""
)