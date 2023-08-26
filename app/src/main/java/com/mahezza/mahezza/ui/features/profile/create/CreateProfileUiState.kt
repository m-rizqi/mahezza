package com.mahezza.mahezza.ui.features.profile.create

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource

data class CreateProfileUiState(
    val id : String = "",

    val photoUrl : String? = null,
    val newPhotoUri : Uri? = null,

    val name : String = "",
    val nameErrorMessage : StringResource? = null,

    val job : String = "",
    val jobErrorMessage : StringResource? = null,

    val selectedRelationshipWithChildrenIndex : Int = 0,
    val relationshipWithChildrenValue : String = "",
    val relationshipWithChildrenErrorMessage : StringResource? = null,

    val selectedTimeSpendWithChildrenIndex : Int = 0,
    val timeSpendWithChildrenValue : String = "",
    val timeSpendWithChildrenErrorMessage : StringResource? = null,

    val generalMessage : StringResource? = null,
    val isShowLoading : Boolean = false,
    val shouldStartAddChildProfile : Boolean = false,

    val relationshipWithChildrenData : List<RelationshipWithChildren> = listOf(
        RelationshipWithChildren(R.string.father, R.drawable.ic_father),
        RelationshipWithChildren(R.string.mother, R.drawable.ic_mother),
        RelationshipWithChildren(R.string.other, R.drawable.ic_other_relationship),
    ),

    val timeSpendWithChildrenData : List<TimeSpendWithChildren> = listOf(
        TimeSpendWithChildren(30, R.string.minute),
        TimeSpendWithChildren(1, R.string.hour),
        TimeSpendWithChildren(2, R.string.hour),
        TimeSpendWithChildren(null, R.string.other),
    )
) {

    data class RelationshipWithChildren(
        @StringRes
        val stringResId : Int,
        @DrawableRes
        val drawableResId : Int
    )
    data class TimeSpendWithChildren(
        val value : Int?,
        @StringRes
        val unitStringResId : Int
    )
}