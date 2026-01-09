package co.hrvoje.data.repositories

import co.hrvoje.domain.models.Move
import co.hrvoje.domain.models.Round
import co.hrvoje.domain.models.User
import co.hrvoje.domain.utils.MoveType

interface MovesRepository {

    suspend fun createMove(
        user: User,
        round: Round,
        choice: MoveType
    ): Move?
}