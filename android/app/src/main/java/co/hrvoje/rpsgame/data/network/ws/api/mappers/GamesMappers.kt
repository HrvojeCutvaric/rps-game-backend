package co.hrvoje.rpsgame.data.network.ws.api.mappers

import android.util.Log
import co.hrvoje.rpsgame.data.network.ws.api.models.games.GameDTO
import co.hrvoje.rpsgame.data.network.ws.api.models.games.GamePlayerDTO
import co.hrvoje.rpsgame.data.network.ws.api.models.games.UserDTO
import co.hrvoje.rpsgame.domain.models.Game
import co.hrvoje.rpsgame.domain.models.GamePlayer
import co.hrvoje.rpsgame.domain.models.User
import co.hrvoje.rpsgame.domain.utils.GameStatus

fun GameDTO.toGame(): Game = Game(
    id = this.id,
    createAt = this.createdAt,
    status = try {
        GameStatus.valueOf(this.state)
    } catch (e: IllegalArgumentException) {
        Log.e("GameDTO", "Invalid game state: ${this.state}")
        GameStatus.WAITING_FOR_PLAYERS
    },
)

fun UserDTO.toUser(): User = User(
    id = this.id,
    username = this.username,
)

fun GamePlayerDTO.toGamePlayer(): GamePlayer = GamePlayer(
    id = this.id,
    user = this.user.toUser(),
    game = this.game.toGame(),
    score = this.score,
    hasCreateGame = this.hasCreateGame,
)
