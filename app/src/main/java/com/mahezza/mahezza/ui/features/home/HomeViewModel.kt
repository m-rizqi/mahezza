package com.mahezza.mahezza.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Game
import com.mahezza.mahezza.data.model.Game.Status.*
import com.mahezza.mahezza.data.repository.GameRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.ui.components.LayoutState
import com.mahezza.mahezza.ui.nav.NavArgumentConst.GAME_ID
import com.mahezza.mahezza.ui.nav.NavArgumentConst.IS_RESUME_GAME
import com.mahezza.mahezza.ui.nav.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val dataStore: MahezzaDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState : StateFlow<HomeUiState>
        get() = _uiState.asStateFlow()

    val parentId = dataStore.firebaseUserIdPreference

    @OptIn(ExperimentalCoroutinesApi::class)
    val lastGameActivityStates : Flow<List<HomeUiState.LastGameActivityState>> = parentId
        .filterNotNull()
        .flatMapLatest {parentId ->
            _uiState.update { it.copy(layoutState = LayoutState.Shimmer) }
            gameRepository.getLastGameActivities(parentId)
        }
        .map {result ->
            if (result is Result.Fail) {
                _uiState.update { it.copy(generalMessage = result.message, layoutState = LayoutState.Empty) }
            }
            val lastGameActivities = result.data?.map {lastGameActivity ->
                HomeUiState.LastGameActivityState(
                    gameId = lastGameActivity.id,
                    children = lastGameActivity.children,
                    puzzle = lastGameActivity.puzzle,
                    lastActivity = lastGameActivity.lastActivity,
                    elapsedTime = lastGameActivity.elapsedTime,
                    onClick = { getResumeDestination(lastGameActivity.status, lastGameActivity.id) }
                )
            } ?: emptyList()
            _uiState.update { it.copy(layoutState = if (lastGameActivities.isEmpty()) LayoutState.Empty else LayoutState.Content) }
            return@map lastGameActivities
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private fun getResumeDestination(status: Game.Status, id: String): String {
        return when(status){
            SelectChild -> Routes.SelectChildForGame
            SelectPuzzle -> Routes.SelectPuzzleForGame
            PlaySession -> "${Routes.PlaySession}?${IS_RESUME_GAME}=${true}&${GAME_ID}=${id}"
            TakeTwibbon -> "${Routes.TakeTwibbon}?${IS_RESUME_GAME}=${true}&${GAME_ID}=${id}"
            Course -> "${Routes.Course}?${IS_RESUME_GAME}=${true}&${GAME_ID}=${id}"
            Finished -> Routes.Home
        }
    }

    fun onEvent(event : HomeEvent){
        when(event){
            HomeEvent.OnGeneralMessageShowed -> _uiState.update { it.copy(generalMessage = null) }
        }
    }
}