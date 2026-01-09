package co.hrvoje.data.db.mappers

import co.hrvoje.data.db.dao.GameDAO
import co.hrvoje.data.db.dao.GamePlayerDAO
import co.hrvoje.data.db.dao.RoundDAO
import co.hrvoje.data.db.dao.UserDAO
import co.hrvoje.domain.models.Game
import co.hrvoje.domain.models.GamePlayer
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
    state = this.state,
    createdAt = this.createdAt.toEpochMilli(),
)

fun Game.toGameDAO(): GameDAO =
    GameDAO.findById(this.id) ?: error("Game not found")

fun GamePlayerDAO.toGamePlayer() = GamePlayer(
    user = this.user.toUser(),
    game = this.game.toGame(),
    score = this.score,
    hasCreatedGamme = this.hasCreatedGame
)

fun RoundDAO.toRound() = Round(
    id = this.id.value,
    game = game.toGame(),
    startedAt = this.startedAt.toEpochMilli(),
)