package com.mahezza.mahezza.data.model

import com.mahezza.mahezza.data.source.firebase.request.CourseRequest
import com.mahezza.mahezza.data.source.firebase.response.CourseResponse


data class Course(
    val name : String,
    val banner : String,
    val id : String,
    val puzzleId : String,
    val subCourses : List<SubCourse>
)

fun CourseResponse.toCourse() : Course = Course(
    name = this.name,
    banner = this.banner,
    id = this.courseId,
    puzzleId = this.puzzleId,
    subCourses = this.subCourses.map { it.toSubCourse() }
)

fun Course.toCourseRequest() : CourseRequest = CourseRequest(
    name = this.name,
    courseId = this.id,
    puzzleId = this.puzzleId,
    banner = this.banner,
    subCourses = this.subCourses.map { it.toSubCourseRequest() }
)