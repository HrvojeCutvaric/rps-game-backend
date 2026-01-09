package co.hrvoje.rpsgame.data.network.ws.api.mappers

import co.hrvoje.rpsgame.data.network.ws.api.models.games.UserDTO
import co.hrvoje.rpsgame.domain.models.User

fun UserDTO.toUser(): User = User(
    id = this.id,
    username = this.username,
)
