package co.hrvoje.rpsgame.data.network.ws.api.models.games

import co.hrvoje.rpsgame.domain.utils.Move
import com.google.gson.annotations.SerializedName

data class RoundDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("game")
    val game: GameDTO,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("first_user_move")
    val firstUserMove: Move?,
    @SerializedName("second_user_move")
    val secondUserMove: Move?,
)
