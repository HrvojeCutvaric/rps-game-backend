package co.hrvoje.data.repositories

import co.hrvoje.domain.models.Round

interface RoundsRepository {

    suspend fun create(gameId: Int): Round?
}