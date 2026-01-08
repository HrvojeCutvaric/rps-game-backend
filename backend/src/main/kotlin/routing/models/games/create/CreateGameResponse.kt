package co.hrvoje.routing.models.games.create

import co.hrvoje.domain.models.Game
import kotlinx.serialization.Serializable

@Serializable
data class CreateGameResponse(
    val game: Game,
)
