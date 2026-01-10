package co.hrvoje.rpsgame.viewmodel.game_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.hrvoje.rpsgame.R
import co.hrvoje.rpsgame.data.network.services.GamesService
import co.hrvoje.rpsgame.domain.models.Game
import co.hrvoje.rpsgame.navigation.AppNavigator
import co.hrvoje.rpsgame.navigation.Route
import co.hrvoje.rpsgame.utils.CurrentUser
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameDetailsViewModel(
    private val appNavigator: AppNavigator,
    private val currentUser: CurrentUser,
    private val gamesService: GamesService,
) : ViewModel() {

    private val game: Game = (appNavigator.backStack.last() as Route.GameDetails).game

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
                currentUser.user?.let { user ->
                    _state.value?.let { currentState ->
                        currentState.game?.let { game ->
                            currentState.rounds?.firstOrNull()?.let { round ->
                                viewModelScope.launch {
                                    gamesService.updateRound(
                                        gameId = game.id,
                                        user = user,
                                        roundId = round.id,
                                        move = action.move,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startPolling() {
        currentUser.user?.let { user ->
            pollingJob = viewModelScope.launch {
                while (isActive) {
                    gamesService.getGameRounds(game.id).fold(
                        onSuccess = { rounds ->
                            val sortedRounds =
                                rounds.sortedByDescending { round -> round.createdAt }
                            val gameFromRound = sortedRounds.firstOrNull()?.game
                            if (gameFromRound == null) {
                                _state.value = GameDetailsState(
                                    game = game,
                                    rounds = emptyList(),
                                    errorResource = null,
                                    isJoinVisible = game.secondUser == null && game.firstUser.id != user.id,
                                    currentUser = user,
                                )
                                return@fold
                            }
                            _state.update {
                                it?.copy(
                                    game = gameFromRound,
                                    rounds = sortedRounds,
                                    errorResource = null,
                                    isJoinVisible = gameFromRound.secondUser == null && gameFromRound.firstUser.id != user.id,
                                ) ?: GameDetailsState(
                                    game = gameFromRound,
                                    rounds = sortedRounds,
                                    errorResource = null,
                                    isJoinVisible = gameFromRound.secondUser == null && gameFromRound.firstUser.id != user.id,
                                    currentUser = user
                                )
                            }
                        },
                        onFailure = {
                            _state.value = GameDetailsState(
                                game = null,
                                rounds = null,
                                errorResource = R.string.generic_error_message,
                                isJoinVisible = false,
                                currentUser = user,
                            )
                        }
                    )
                    delay(1000)
                }
            }
        }
    }
}
