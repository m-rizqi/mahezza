package com.mahezza.mahezza.ui.features.children.list

import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.ui.components.LayoutState

data class ChildrenListState (
    val generalMessage : StringResource? = null,
    val childrenLayoutState : LayoutState = LayoutState.Shimmer,
) {
    data class ChildrenSummaryState (
        val name : String,
        val photoUrl : String,
        val age : Double,
        val lastActivity : String,
        val numberOfPlay : Int,
        val timeOfPlay : Int,
        val numberOfCompletedChallenge : Int,
    )
}