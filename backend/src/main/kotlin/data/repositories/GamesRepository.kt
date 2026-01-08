package co.hrvoje.data.repositories

import co.hrvoje.domain.models.Game
import co.hrvoje.domain.utils.GameState

interface GamesRepository {
    suspend fun create(): Game?

    suspend fun getGames(state: GameState?): List<Game>

    suspend fun update(game: Game): Game?

    suspend fun getGameById(id: Int): Game?
}