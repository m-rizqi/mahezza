package com.mahezza.mahezza.data.source.firebase.request

data class CourseRequest(
    val name : String,
    val courseId : String,
    val puzzleId : String,
    val subCourses : List<SubCourseRequest>,
    val banner : String
)