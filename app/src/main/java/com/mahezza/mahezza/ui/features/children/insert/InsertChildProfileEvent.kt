package com.mahezza.mahezza.ui.features.children.insert

import android.net.Uri
import java.time.LocalDate

sealed class InsertChildProfileEvent {
    class OnImagePicked(val uri : Uri) : InsertChildProfileEvent()
    class OnNameChanged(val value : String) : InsertChildProfileEvent()
    class OnGenderChanged(val index : Int, val value: String) : InsertChildProfileEvent()
    class OnBirthDateChanged(val value: LocalDate) : InsertChildProfileEvent()
    object OnSaveAndInsertMore : InsertChildProfileEvent()
    object OnSaveAndNext : InsertChildProfileEvent()
    object OnGeneralMessageShowed : InsertChildProfileEvent()
    object OnDashboardStarted : InsertChildProfileEvent()
}