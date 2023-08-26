package com.mahezza.mahezza.ui.features.children.insert

import android.net.Uri
import com.mahezza.mahezza.common.StringResource
import java.time.LocalDate

data class InsertChildProfileUiState(
    val newPhotoUri : Uri? = null,
    val savedChildPhotoUrls : MutableList<String> = mutableListOf(),

    val name : String = "",
    val nameErrorMessage : StringResource? = null,

    val gender : String = "",
    val selectedGenderIndex: Int = 0,

    val pickedBirthDate: LocalDate = LocalDate.now(),
    val birthDate : String = "",
    val age : Double = 0.0,

    val generalErrorMessage : StringResource? = null,
    val isShowLoading : Boolean = false,
    val shouldStartRedeemPuzzleScreen : Boolean = false
)