package co.hrvoje.rpsgame.navigation

import androidx.navigation3.runtime.NavKey
import co.hrvoje.rpsgame.domain.models.Game
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {

    @Serializable
    data object Login : Route

    @Serializable
    data object Register : Route

    @Serializable
    data object Games : Route

    @Serializable
    data class GameDetails(val game: Game) : Route
}
