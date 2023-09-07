package com.mahezza.mahezza.ui.features.game.course

sealed class CourseEvent {
    object OnGeneralMessageShowed : CourseEvent()
    class SetPuzzleId(val puzzleId: String) : CourseEvent()
    object OnSubCourseDetailOpened : CourseEvent()
    class OpenSubCourse(val subCourseId : String) : CourseEvent()
    class OnSubCourseCompleted(val subCourseId: String) : CourseEvent()
}