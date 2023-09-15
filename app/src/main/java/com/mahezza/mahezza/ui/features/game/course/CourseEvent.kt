package com.mahezza.mahezza.ui.features.game.course

import com.mahezza.mahezza.data.model.Course

sealed class CourseEvent {
    object OnGeneralMessageShowed : CourseEvent()
    class SetPuzzleToGetCourse(val puzzleId: String, val course: Course?) : CourseEvent()
    object OnSubCourseDetailOpened : CourseEvent()
    class OpenSubCourse(val subCourseId : String) : CourseEvent()
    class OnSubCourseCompleted(val subCourseId: String) : CourseEvent()
    class SetResumeCourse(val course: Course?): CourseEvent()
}