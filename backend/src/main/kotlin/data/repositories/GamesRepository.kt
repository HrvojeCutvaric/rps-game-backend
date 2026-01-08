package co.hrvoje.data.repositories

import co.hrvoje.domain.models.Game

interface GamesRepository {
    suspend fun create(): Game?
}