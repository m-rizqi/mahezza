package com.mahezza.mahezza.ui.features.game.selectchild

import androidx.lifecycle.ViewModel
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.repository.ChildrenRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.domain.common.CalculateAgeUseCase
import com.mahezza.mahezza.domain.common.FormatDateUseCase
import com.mahezza.mahezza.ui.components.LayoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SelectChildForGameViewModel @Inject constructor(
    private val childrenRepository: ChildrenRepository,
    private val dataStore: MahezzaDataStore,
): ViewModel() {
    private val calculateAgeUseCase = CalculateAgeUseCase()
    private val formatDateUseCase = FormatDateUseCase()

    private val _uiState = MutableStateFlow(SelectChildForGameUiState())
    val uiState : StateFlow<SelectChildForGameUiState>
        get() = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val childrenResults = dataStore.firebaseUserIdPreference
        .filterNotNull()
        .flatMapLatest { parentId ->
            _uiState.update {
                it.copy(childLayoutState = LayoutState.Shimmer)
            }
            childrenRepository.getAllChild(parentId)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val childrenCardStates = childrenResults.flatMapLatest { result ->
        when (result) {
            is Result.Fail -> {
                _uiState.update {
                    it.copy(
                        generalMessage = result.message
                    )
                }
                _uiState.update {
                    it.copy(
                        selectChildCardForGameStateList = emptyList(),
                        childLayoutState = LayoutState.Empty,
                        children = emptyList()
                    )
                }
                flowOf(emptyList())
            }
            is Result.Success -> {
                _uiState.update { it.copy(children = result.data ?: emptyList()) }
                val childCardStates = result.data?.map { child ->
                    val childFromUiState = _uiState.value.selectChildCardForGameStateList.find { it.id == child.id }
                    SelectChildForGameUiState.SelectChildCardForGameState(
                        id = child.id,
                        imageUrl = child.photoUrl,
                        name = child.name,
                        age = formatDateUseCase.parse(child.birthdate)?.let {
                            calculateAgeUseCase(
                                it
                            )
                        } ?: 0.0,
                        lastActivity = child.lastActivity,
                        isChecked = childFromUiState?.isChecked ?: false,
                        onCheckedChangeListener = { isChecked ->
                            onCheckedChildChanged(child.id, isChecked)
                        }
                    )
                } ?: emptyList()
                _uiState.update {
                    it.copy(
                        selectChildCardForGameStateList = childCardStates,
                        childLayoutState = if (childCardStates.isEmpty()) LayoutState.Empty else LayoutState.Content
                    )
                }
                flowOf(childCardStates)
            }
        }
    }

    fun onEvent(event: SelectChildForGameEvent){
        when(event){
            is SelectChildForGameEvent.OnCheckedChildChanged -> onCheckedChildChanged(event.childId, event.isChecked)
            SelectChildForGameEvent.OnGeneralMessageShowed -> _uiState.update { it.copy(generalMessage = null) }
            SelectChildForGameEvent.OnNextListener -> validateSelectedChildForGame()
            SelectChildForGameEvent.OnNavigatedToSelectPuzzle -> _uiState.update { it.copy(finalSelectedChildren = null) }
        }
    }

    private fun validateSelectedChildForGame() {
        _uiState.update { it.copy(isShowLoading = true) }
        val selectedChildCardStateList = uiState.value.selectChildCardForGameStateList.filter { it.isChecked }
        val selectedChildren = uiState.value.children.filter { child ->
            child.id in selectedChildCardStateList.map {childCardState ->
                childCardState.id
            }
        }
        if (selectedChildren.isEmpty()){
            _uiState.update { it.copy(
                isShowLoading = false,
                generalMessage = StringResource.StringResWithParams(R.string.a_child_must_selected)
            ) }
            return
        }
        _uiState.update {
            it.copy(isShowLoading = false, finalSelectedChildren = selectedChildren)
        }
    }

    private fun onCheckedChildChanged(childId: String, isChecked: Boolean) {
        val updatedList = uiState.value.selectChildCardForGameStateList.map { childCard ->
            if (childCard.id == childId) {
                childCard.copy(isChecked = isChecked)
            } else {
                childCard
            }
        }

        _uiState.update {
            it.copy(selectChildCardForGameStateList = updatedList)
        }
    }
}