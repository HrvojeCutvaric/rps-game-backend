package co.hrvoje.data.db.mappers

import co.hrvoje.data.db.dao.GameDAO
import co.hrvoje.data.db.dao.RoundDAO
import co.hrvoje.data.db.dao.UserDAO
import co.hrvoje.domain.models.Game
import co.hrvoje.domain.models.Round
import co.hrvoje.domain.models.User
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun UserDAO.toUser() = User(
    id = this.id.value,
    username = this.username,
    password = this.password,
)

fun User.toUserDAO(): UserDAO =
    UserDAO.findById(this.id) ?: error("User not found")

fun GameDAO.toGame() = Game(
    id = this.id.value,
    createdAt = this.createdAt.toEpochMilli(),
    firstUser = this.firstUser.toUser().copy(password = ""),
    secondUser = this.secondUser?.toUser()?.copy(password = ""),
)

fun RoundDAO.toRound() = Round(
    id = this.id.value,
    game = game.toGame(),
    createdAt = this.createdAt.toEpochMilli(),
    firstUserMove = this.firstUserMove,
    secondUserMove = this.secondUserMove
)