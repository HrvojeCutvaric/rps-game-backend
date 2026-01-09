package co.hrvoje.data.repositories

import co.hrvoje.data.db.dao.MoveDAO
import co.hrvoje.data.db.mappers.suspendTransaction
import co.hrvoje.data.db.mappers.toMove
import co.hrvoje.data.db.mappers.toRoundDAO
import co.hrvoje.data.db.mappers.toUserDAO
import co.hrvoje.domain.models.Move
import co.hrvoje.domain.models.Round
import co.hrvoje.domain.models.User
import co.hrvoje.domain.utils.MoveType

class MovesRepositoryImpl : MovesRepository {

    override suspend fun createMove(
        user: User,
        round: Round,
        choice: MoveType
    ): Move? = suspendTransaction {
        try {
            MoveDAO.new {
                this.user = user.toUserDAO()
                this.round = round.toRoundDAO()
                this.choice = choice
            }.toMove()
        } catch (e: Throwable) {
            println("Error while creating move: ${e.message}")
            null
        }
    }
}