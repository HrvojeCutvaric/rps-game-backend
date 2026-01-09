package co.hrvoje.rpsgame.data.network.ws.api.mappers

import co.hrvoje.rpsgame.data.network.ws.api.models.games.RoundDTO
import co.hrvoje.rpsgame.domain.models.Round

fun RoundDTO.toRound(): Round = Round(
    id = this.id,
    game = this.game.toGame(),
    createdAt = this.createdAt,
    firstUserMove = this.firstUserMove,
    secondUserMove = this.secondUserMove,
)
