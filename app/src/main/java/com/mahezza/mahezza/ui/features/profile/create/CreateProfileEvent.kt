package com.mahezza.mahezza.ui.features.profile.create

import android.net.Uri

sealed class CreateProfileEvent {
    class OnRelationshipWithChildrenIndexChanged(val index: Int) : CreateProfileEvent()
    class OnRelationshipWithChildrenValueChanged(val value : String) : CreateProfileEvent()
    class OnTimeSpendWithChildrenChanged(val index: Int) : CreateProfileEvent()
    class OnTimeSpendWithChildrenValueChanged(val value : String) : CreateProfileEvent()
    class OnNameValueChanged(val value : String) : CreateProfileEvent()
    class OnJobValueChanged(val value: String) : CreateProfileEvent()
    class SetUserId(val id : String) : CreateProfileEvent()
    object OnGeneralMessageShowed : CreateProfileEvent()
    class SetPhotoProfileUri(val uri: Uri) : CreateProfileEvent()
    object OnSaveAndNextButtonClicked : CreateProfileEvent()
    object OnStartAddChildrenProfileScreen : CreateProfileEvent()

}