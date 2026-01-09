package co.hrvoje.rpsgame.data.network.ws.api.models.games

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class GameDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("first_user")
    val firstUser: UserDTO,
    @SerializedName("second_user")
    val secondUser: UserDTO?,
)
