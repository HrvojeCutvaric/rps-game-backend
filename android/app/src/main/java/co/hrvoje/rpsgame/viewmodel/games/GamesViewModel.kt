package co.hrvoje.rpsgame.viewmodel.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.hrvoje.rpsgame.R
import co.hrvoje.rpsgame.data.network.services.GamesService
import co.hrvoje.rpsgame.domain.utils.GameStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GamesViewModel(
    private val gamesService: GamesService,
) : ViewModel() {

    private val _state = MutableStateFlow<GamesState?>(null)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            gamesService.getGames(GameStatus.WAITING_FOR_PLAYERS).fold(
                onSuccess = {
                    _state.value = GamesState(
                        games = it,
                        errorResource = null,
                    )
                },
                onFailure = {
                    GamesState(
                        games = emptyList(),
                        errorResource = R.string.generic_error_message,
                    )
                }
            )
        }
    }

    fun execute(action: GamesAction) {
        when (action) {
            GamesAction.OnGameCreateClicked -> {
                // TODO: implement on game create clicked
            }

            is GamesAction.OnGamesJoinClicked -> {
                // TODO: implement on games join clicked
            }
        }
    }
}
