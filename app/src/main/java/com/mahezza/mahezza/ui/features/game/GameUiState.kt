package com.mahezza.mahezza.ui.features.game

import android.graphics.Bitmap
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Course
import com.mahezza.mahezza.data.model.Game
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.ui.features.game.course.CourseUiState

data class GameUiState(
    val id : String? = null,
    val children: List<Child> = listOf(
//        Child(
//            parentId = "8XaGJ0QB9JMe9wD8Juh7RqZjSoX2",
//            id = "0f98f1c7-cce9-4709-a0a4-71b9e4ca111b",
//            name = "Aliya Agnesa",
//            gender = "Perempuan",
//            birthdate = "11 Aug 2017",
//            photoUrl = "https://images.unsplash.com/photo-1554342321-0776d282ceac?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=387&q=80",
//            lastActivity = "Foto Bersama"
//        )
    ),
    val puzzle: Puzzle? = null,
//    Puzzle(
//    id = "a80d4898-4278-11ee-be56-0242ac120002",
//    name = "Mahezza Family",
//    banner = "https://firebasestorage.googleapis.com/v0/b/mahezza-c3ed3.appspot.com/o/puzzles%2Fa80d4898-4278-11ee-be56-0242ac120002%2FGroup%20100.png?alt=media&token=4e26ff2b-8edd-42f1-8bba-9426b2ccce71",
//    songs = listOf(
//        Song(
//            id = "bytbuYmKSKrVa7vrF4iM",
//            songUrl = "https://firebasestorage.googleapis.com/v0/b/mahezza-c3ed3.appspot.com/o/puzzles%2Fa80d4898-4278-11ee-be56-0242ac120002%2FSayang%20Semuanya.mp3?alt=media&token=895431e5-1182-4513-99fb-c7043d7e9a1b",
//            title = "Sayang Semuanya",
//            lyrics = listOf(
//                "Satu satu aku sayang ibu",
//                "Dua-dua juga sayang ayah",
//                "Tiga-tiga sayang adik kakak",
//                "Satu dua tiga sayang semuanya"
//            ),
//            typeOrArtist = "Lagu Anak-Anak"
//        )
//    ),
//    illustrationUrl = "https://firebasestorage.googleapis.com/v0/b/mahezza-c3ed3.appspot.com/o/puzzles%2Fa80d4898-4278-11ee-be56-0242ac120002%2Fgradient_mesh_anim.gif?alt=media&token=d66ce88a-290c-4401-a562-7bd95e9a7a7d",
//    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Vulputate eu scelerisque felis imperdiet. Nisl purus in mollis nunc sed id semper. Netus et malesuada fames ac turpis egestas sed. Aliquam nulla facilisi cras fermentum odio eu feugiat. Sit amet mauris commodo quis imperdiet massa tincidunt nunc. Id donec ultrices tincidunt arcu non sodales. Felis bibendum ut tristique et egestas quis ipsum. Facilisis magna etiam tempor orci eu lobortis. Enim neque volutpat ac tincidunt. Dui faucibus in ornare quam viverra orci. Tristique senectus et netus et malesuada fames ac turpis. Sit amet purus gravida quis blandit. At consectetur lorem donec massa sapien faucibus et. Vestibulum rhoncus est pellentesque elit ullamcorper dignissim cras.",
//    twibbonUrl = "https://firebasestorage.googleapis.com/v0/b/mahezza-c3ed3.appspot.com/o/puzzles%2Fa80d4898-4278-11ee-be56-0242ac120002%2FTwibbon.png?alt=media&token=8daef3dc-0a0a-4d98-befa-99f556413da5"
//)
    val elapsedTime : String = "00:00:00",
    val lastActivity : String = "",
    val twibbon : Bitmap? = null,
    val twibbonUrl : String? = null,
    val courseState: CourseUiState.CourseState? = null,
    val isGameFinished : Boolean = false,
    val course : Course? = null,

    val isLoading : Boolean = false,
    val generalMessage : StringResource? = null,
    val acknowledgeCode : AcknowledgeCode? = null,
    val status : Game.Status? = null,
    val game: Game? = null
){
    enum class AcknowledgeCode {
        CHILDREN,
        PUZZLE,
        PLAY_SESSION_AND_EXIT,
        PLAY_SESSION_AND_NEXT,
        TWIBBON_DOWNLOAD,
        TWIBBON_SHARE,
        TWIBBON_AND_NEXT,
        TWIBBON_AND_EXIT,
        COURSE_EXIT,
        COURSE_FINISHED
    }

    fun getElapsedTimeInMinute() : Float {
        return convertTimeStringToFloat(this.elapsedTime)
    }

    private fun convertTimeStringToFloat(timeString: String): Float {
        val parts = timeString.split(":")
        if (parts.size != 3) {
            throw IllegalArgumentException("Invalid time format: $timeString")
        }

        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()
        val seconds = parts[2].toInt()

        val totalMinutes = hours * 60 + minutes + seconds / 60.0f

        return totalMinutes.toFloat()
    }

}