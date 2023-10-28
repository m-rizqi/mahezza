package com.mahezza.mahezza.ui.features.children.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.LastGameActivity
import com.mahezza.mahezza.data.repository.ChildrenRepository
import com.mahezza.mahezza.data.repository.GameRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.domain.common.CalculateAgeUseCase
import com.mahezza.mahezza.domain.common.FormatDateUseCase
import com.mahezza.mahezza.ui.components.LayoutState
import com.mahezza.mahezza.ui.features.game.selectchild.SelectChildForGameEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ChildrenListViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    dataStore: MahezzaDataStore,
    private val childrenRepository: ChildrenRepository,
) : ViewModel() {

    private val formatDateUseCase = FormatDateUseCase()
    private val calculateAgeUseCase = CalculateAgeUseCase()

    private val _uiState = MutableStateFlow(ChildrenListState())
    val uiState: StateFlow<ChildrenListState>
        get() = _uiState.asStateFlow()

    val parentId = dataStore.firebaseUserIdPreference

    private val childrens: Flow<List<Child>> = parentId
        .filterNotNull()
        .flatMapLatest { parentId ->
            childrenRepository.getAllChild(parentId)
        }.map { result ->
            return@map result.data ?: emptyList()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val allGameActivities: Flow<List<LastGameActivity>> = parentId
        .filterNotNull()
        .flatMapLatest { parentId ->
            gameRepository.getAllGameActivities(parentId)
        }
        .map { result ->
            return@map result.data ?: emptyList()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val childrenListStates: Flow<List<ChildrenListState.ChildrenSummaryState>> =
        combine(childrens, allGameActivities) { children, lastGameActivities ->
            _uiState.update {
                it.copy(
                    childrenLayoutState = LayoutState.Content,
                )
            }
            return@combine children.map { child ->
                val numberOfPlay = lastGameActivities.count { gameActivity ->
                    gameActivity.children.any {
                        it.id == child.id
                    }
                }
                val timeOfPlay = lastGameActivities
                    .filter { gameActivity ->
                        gameActivity.children.any {
                            it.id == child.id
                        }
                    }.sumOf { calculateTotalMinutes(it.elapsedTime) }
                val lastGameActivity = lastGameActivities.filter { gameActivity ->
                    gameActivity.children.any {
                        it.id == child.id
                    }
                }.firstOrNull()

                ChildrenListState.ChildrenSummaryState(
                    name = child.name,
                    photoUrl = child.photoUrl,
                    age = calculateAgeUseCase(
                        formatDateUseCase.parse(child.birthdate) ?: LocalDate.now()
                    ),
                    lastActivity = lastGameActivity?.lastActivity ?: "-",
                    numberOfPlay = numberOfPlay,
                    timeOfPlay = timeOfPlay,
                    numberOfCompletedChallenge = timeOfPlay
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private fun calculateTotalMinutes(timeString: String): Int {
        try {
            val parts = timeString.split(":")

            if (parts.size != 3) {
                return 0
            }

            val hours = parts[0].toInt()
            val minutes = parts[1].toInt()
            val seconds = parts[2].toInt()

            return hours * 60 + minutes + seconds / 60
        } catch (e : Exception){
            return 0
        }
    }

    fun onEvent(event: ChildrenListEvent){
        when(event){
            ChildrenListEvent.OnGeneralMessageShowed -> _uiState.update { it.copy(generalMessage = null) }
        }
    }

}