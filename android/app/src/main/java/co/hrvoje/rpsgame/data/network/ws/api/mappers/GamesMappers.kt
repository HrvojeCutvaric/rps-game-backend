package co.hrvoje.rpsgame.data.network.ws.api.mappers

import co.hrvoje.rpsgame.data.network.ws.api.models.games.GameDTO
import co.hrvoje.rpsgame.domain.models.Game

fun GameDTO.toGame(): Game = Game(
    id = this.id,
    createAt = this.createdAt,
    firstUser = this.firstUser.toUser(),
    secondUser = this.secondUser?.toUser(),
)
