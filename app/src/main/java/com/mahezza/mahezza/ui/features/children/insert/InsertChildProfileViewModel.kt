package com.mahezza.mahezza.ui.features.children.insert

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.domain.KeyValue
import com.mahezza.mahezza.domain.ResultWithKeyMessages
import com.mahezza.mahezza.domain.children.InsertChildUseCase
import com.mahezza.mahezza.domain.common.CalculateAgeUseCase
import com.mahezza.mahezza.domain.common.FormatDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class InsertChildProfileViewModel @Inject constructor(
    private val resources: Resources,
    private val insertChildUseCase: InsertChildUseCase
): ViewModel() {

    private val formatDateUseCase = FormatDateUseCase()
    private val calculateAgeUseCase = CalculateAgeUseCase()

    private val _uiState = MutableStateFlow(InsertChildProfileUiState())
    val uiState : StateFlow<InsertChildProfileUiState>
        get() = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                gender = resources.getString(R.string.boy),
                birthDate = formatDateUseCase.format(LocalDate.now())
            )
        }
    }

    fun onEvent(event : InsertChildProfileEvent){
        when(event){
            is InsertChildProfileEvent.OnBirthDateChanged -> setBirthDate(event.value)
            is InsertChildProfileEvent.OnGenderChanged -> _uiState.update { it.copy(selectedGenderIndex = event.index, gender = event.value) }
            is InsertChildProfileEvent.OnImagePicked -> _uiState.update { it.copy(newPhotoUri = event.uri) }
            is InsertChildProfileEvent.OnNameChanged -> _uiState.update { it.copy(name = event.value, nameErrorMessage = null) }
            InsertChildProfileEvent.OnSaveAndInsertMore -> saveAndAddMoreChildProfile()
            InsertChildProfileEvent.OnSaveAndNext -> saveAndNext()
            InsertChildProfileEvent.OnGeneralMessageShowed -> _uiState.update { it.copy(generalErrorMessage = null) }
            InsertChildProfileEvent.OnRedeemPuzzleStarted -> _uiState.update { it.copy(shouldStartRedeemPuzzleScreen = false) }
        }
    }

    private fun setBirthDate(birthdate: LocalDate) {
        val age = calculateAgeUseCase(birthdate)
        _uiState.update {
            it.copy(birthDate = formatDateUseCase.format(birthdate), age = age, pickedBirthDate = birthdate)
        }
    }

    private fun saveAndAddMoreChildProfile(){
        viewModelScope.launch {
            _uiState.update { it.copy(isShowLoading = true) }
            val result = save()
            if (result != null){
                val savedChildPhotoUrls = _uiState.value.savedChildPhotoUrls
                savedChildPhotoUrls.add(result)
                _uiState.update {
                    it.copy(
                        savedChildPhotoUrls = savedChildPhotoUrls,
                        newPhotoUri = null,
                        name = "",
                        gender = resources.getString(R.string.boy),
                        birthDate = formatDateUseCase.format(LocalDate.now()),
                        age = 0.0,
                        pickedBirthDate = LocalDate.now(),
                        selectedGenderIndex = 0,
                    )
                }
            }
            _uiState.update { it.copy(isShowLoading = false) }
        }
    }

    private fun saveAndNext(){
        viewModelScope.launch {
            _uiState.update { it.copy(isShowLoading = true) }
//            val result = save()
//            if (result != null){
                _uiState.update {
                    it.copy(
                        shouldStartRedeemPuzzleScreen = true,
                    )
                }
//            }
            _uiState.update { it.copy(isShowLoading = false) }
        }
    }

    private suspend fun save() : String? {
        val insertChildData = getInsertChildData()
        val result = insertChildUseCase(insertChildData)
        return when(result){
            is ResultWithKeyMessages.Fail -> {
                assignErrorsToEachField(insertChildData, result.messages)
                null
            }
            is ResultWithKeyMessages.Success -> result.data
        }
    }

    private fun getInsertChildData() : InsertChildUseCase.InsertChildData {
        val uiStateValue = uiState.value
        return InsertChildUseCase.InsertChildData(
            parentId = "",
            id = "",
            name = KeyValue("name", uiStateValue.name),
            gender = uiStateValue.gender,
            birthdate = uiStateValue.birthDate,
            photoUri = uiStateValue.newPhotoUri
        )
    }

    private fun assignErrorsToEachField(insertChildData: InsertChildUseCase.InsertChildData, errors : List<KeyValue<StringResource?>>?){
        errors ?: return
        _uiState.update {
            it.copy(
                nameErrorMessage = findErrorWithKey(errors, insertChildData.name.key),
                generalErrorMessage = findErrorWithKey(errors, "general")
            )
        }
    }

    private fun findErrorWithKey(errors: List<KeyValue<StringResource?>>?, key : String) : StringResource? {
        return errors?.find {
            it.key == key
        }?.value
    }
}