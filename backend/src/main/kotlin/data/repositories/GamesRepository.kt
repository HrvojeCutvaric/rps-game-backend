package co.hrvoje.data.repositories

import co.hrvoje.domain.models.Game
import co.hrvoje.domain.models.User

interface GamesRepository {
    suspend fun create(user: User): Game?

    suspend fun getGames(): List<Game>

    suspend fun update(game: Game): Game?

    suspend fun getGameById(id: Int): Game?
}