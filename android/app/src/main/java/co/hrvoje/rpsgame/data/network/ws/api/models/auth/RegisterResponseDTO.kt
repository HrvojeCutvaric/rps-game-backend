package co.hrvoje.rpsgame.data.network.ws.api.models.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponseDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("username")
    val username: String,
)
