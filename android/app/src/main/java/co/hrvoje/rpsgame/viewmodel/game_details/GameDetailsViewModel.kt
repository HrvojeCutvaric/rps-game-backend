package co.hrvoje.rpsgame.viewmodel.game_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.hrvoje.rpsgame.R
import co.hrvoje.rpsgame.data.network.services.GamesService
import co.hrvoje.rpsgame.domain.models.Game
import co.hrvoje.rpsgame.navigation.AppNavigator
import co.hrvoje.rpsgame.utils.CurrentUser
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameDetailsViewModel(
    private val game: Game,
    private val appNavigator: AppNavigator,
    private val currentUser: CurrentUser,
    private val gamesService: GamesService,
) : ViewModel() {

    private val _state = MutableStateFlow<GameDetailsState?>(null)
    val state = _state.asStateFlow()

    private var pollingJob: Job? = null

    init {
        startPolling()
    }

    fun execute(action: GameDetailsAction) {
        when (action) {
            GameDetailsAction.OnBackClicked -> {
                pollingJob?.cancel()
                appNavigator.navigateBack()
            }

            GameDetailsAction.OnJoinClicked -> {
                currentUser.user?.let { user ->
                    _state.value?.let { currentState ->
                        currentState.game?.let { game ->
                            viewModelScope.launch {
                                gamesService.joinGame(
                                    gameId = game.id,
                                    user = user,
                                )
                            }
                        }
                    }
                }
            }

            is GameDetailsAction.OnMoveClicked -> {
                // TODO: implement on move clicked
            }
        }
    }

    private fun startPolling() {
        pollingJob = viewModelScope.launch {
            while (isActive) {
                gamesService.getGameRounds(game.id).fold(
                    onSuccess = {
                        val rounds = it.sortedByDescending { round -> round.createdAt }

                        _state.value = GameDetailsState(
                            game = game,
                            rounds = it.sortedByDescending { round -> round.createdAt },
                            errorResource = null,
                            isJoinVisible =
                                game.secondUser == null &&
                                        game.firstUser.id != currentUser.user?.id
                        )
                    },
                    onFailure = {
                        _state.value = GameDetailsState(
                            game = null,
                            rounds = null,
                            errorResource = R.string.generic_error_message,
                            isJoinVisible = false
                        )
                    }
                )
                delay(1000)
            }
        }
    }
}
