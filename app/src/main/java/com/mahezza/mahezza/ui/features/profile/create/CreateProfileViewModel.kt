package com.mahezza.mahezza.ui.features.profile.create

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.domain.KeyValue
import com.mahezza.mahezza.domain.Result
import com.mahezza.mahezza.domain.ResultWithKeyMessages
import com.mahezza.mahezza.domain.user.CreateProfileUseCase
import com.mahezza.mahezza.domain.user.CreateProfileUseCaseImpl
import com.mahezza.mahezza.domain.user.GetUserByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val resources: Resources,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val createProfileUseCase: CreateProfileUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateProfileUiState())
    val uiState : StateFlow<CreateProfileUiState>
        get() = _uiState.asStateFlow()

    init {
        val initialRelationshipWithChildren = uiState.value.relationshipWithChildrenData[uiState.value.selectedRelationshipWithChildrenIndex]
        val initialTimeSpendWithChildren = uiState.value.timeSpendWithChildrenData[uiState.value.selectedTimeSpendWithChildrenIndex]
        _uiState.update {
            it.copy(
                relationshipWithChildrenValue = constructRelationshipWithChildrenValue(
                    initialRelationshipWithChildren
                ),
                timeSpendWithChildrenValue = constructTimeSpendWithChildrenValue(
                    initialTimeSpendWithChildren
                )
            )
        }
    }

    fun onEvent(event: CreateProfileEvent){
        when(event){
            is CreateProfileEvent.OnRelationshipWithChildrenIndexChanged -> onRelationshipWithChildrenChange(event.index)
            is CreateProfileEvent.OnTimeSpendWithChildrenChanged -> onTimeSpendWithChildrenChange(event.index)
            is CreateProfileEvent.OnRelationshipWithChildrenValueChanged -> _uiState.update { it.copy(relationshipWithChildrenValue = event.value, relationshipWithChildrenErrorMessage = null) }
            is CreateProfileEvent.OnNameValueChanged -> _uiState.update { it.copy(name = event.value, nameErrorMessage = null) }
            is CreateProfileEvent.OnTimeSpendWithChildrenValueChanged -> _uiState.update { it.copy(timeSpendWithChildrenValue = event.value, timeSpendWithChildrenErrorMessage = null) }
            is CreateProfileEvent.OnJobValueChanged -> _uiState.update { it.copy(job = event.value, jobErrorMessage = null) }
            is CreateProfileEvent.SetUserId -> getUserById(event.id)
            is CreateProfileEvent.OnGeneralMessageShowed -> _uiState.update { it.copy(generalMessage = null) }
            is CreateProfileEvent.SetPhotoProfileUri -> _uiState.update { it.copy(
                newPhotoUri = event.uri
            ) }
            is CreateProfileEvent.OnSaveAndNextButtonClicked -> saveAndNext()
            CreateProfileEvent.OnStartAddChildrenProfileScreen -> _uiState.update { it.copy(shouldStartAddChildProfile = false) }
        }
    }

    private fun saveAndNext() {
        viewModelScope.launch {
            _uiState.update { it.copy(isShowLoading = true) }
            val createProfileData = getCreateProfileData()
            val resultWithMessages = createProfileUseCase.invoke(createProfileData)
            when(resultWithMessages){
                is ResultWithKeyMessages.Fail -> assignErrorsToEachField(createProfileData, resultWithMessages.messages)
                is ResultWithKeyMessages.Success -> {
                    _uiState.update {
                        it.copy(
                            shouldStartAddChildProfile = true
                        )
                    }
                }
            }
            _uiState.update { it.copy(isShowLoading = false) }
        }
    }

    private fun assignErrorsToEachField(createProfileData: CreateProfileUseCase.CreateProfileData, errors : List<KeyValue<StringResource?>>?){
        errors ?: return
        _uiState.update {
            it.copy(
                nameErrorMessage = findErrorWithKey(errors, createProfileData.name.key),
                jobErrorMessage = findErrorWithKey(errors, createProfileData.job.key),
                relationshipWithChildrenErrorMessage = findErrorWithKey(errors, createProfileData.relationshipWithChildren.key),
                timeSpendWithChildrenErrorMessage = findErrorWithKey(errors, createProfileData.timeSpendWithChildren.key),
                generalMessage = findErrorWithKey(errors, "general")
            )
        }
    }

    private fun findErrorWithKey(errors: List<KeyValue<StringResource?>>?, key : String) : StringResource? {
        return errors?.find {
            it.key == key
        }?.value
    }

    private fun getCreateProfileData(): CreateProfileUseCase.CreateProfileData {
        val uiStateValue = uiState.value
        return CreateProfileUseCase.CreateProfileData(
            id = uiStateValue.id,
            name = KeyValue("name", uiStateValue.name),
            job = KeyValue("job", uiStateValue.job),
            relationshipWithChildren = KeyValue("relationship_with_children", uiStateValue.relationshipWithChildrenValue),
            timeSpendWithChildren = KeyValue("time_spend_with_children", uiStateValue.timeSpendWithChildrenValue),
            newPhotoUri = uiStateValue.newPhotoUri,
            photoUrl = uiStateValue.photoUrl
        )
    }

    private fun getUserById(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isShowLoading = true, id = id) }
            val result = getUserByIdUseCase.invoke(id)
            when(result){
                is Result.Fail -> _uiState.update {
                    it.copy(generalMessage = result.message)
                }
                is Result.Success -> {
                    result.data?.let { user ->
                        _uiState.update {
                            it.copy(
                                name = user.name,
                                photoUrl = user.photoUrl
                            )
                        }
                    }
                }
            }
            _uiState.update { it.copy(isShowLoading = false) }
        }
    }

    private fun onTimeSpendWithChildrenChange(index: Int) {
        var timeSpendWithChildrenValue = constructTimeSpendWithChildrenValue(
            uiState.value.timeSpendWithChildrenData[index]
        )
        if (isLastIndexOfTimeSpendWithChildrenData(index)){
            timeSpendWithChildrenValue = ""
        }
        _uiState.update { it.copy(selectedTimeSpendWithChildrenIndex = index, timeSpendWithChildrenValue = timeSpendWithChildrenValue) }
    }

    private fun isLastIndexOfTimeSpendWithChildrenData(index: Int): Boolean = index == uiState.value.timeSpendWithChildrenData.lastIndex

    private fun onRelationshipWithChildrenChange(index: Int) {
        var relationshipWithChildrenValue = constructRelationshipWithChildrenValue(
            uiState.value.relationshipWithChildrenData[index]
        )
        if (isLastIndexOfRelationshipWithChildrenData(index)){
            relationshipWithChildrenValue = ""
        }
        _uiState.update {
            it.copy(selectedRelationshipWithChildrenIndex = index, relationshipWithChildrenValue = relationshipWithChildrenValue)
        }
    }

    private fun isLastIndexOfRelationshipWithChildrenData(index: Int) : Boolean = index == uiState.value.relationshipWithChildrenData.lastIndex

    private fun constructRelationshipWithChildrenValue(relationshipWithChildren: CreateProfileUiState.RelationshipWithChildren): String {
        return resources.getString(relationshipWithChildren.stringResId)
    }

    private fun constructTimeSpendWithChildrenValue(timeSpendWithChildren: CreateProfileUiState.TimeSpendWithChildren): String {
        return "${timeSpendWithChildren.value ?: ""} ${resources.getString(timeSpendWithChildren.unitStringResId)}"
    }
}