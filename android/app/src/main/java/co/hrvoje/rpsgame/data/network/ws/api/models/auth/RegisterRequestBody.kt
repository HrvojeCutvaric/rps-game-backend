package co.hrvoje.rpsgame.data.network.ws.api.models.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestBody(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String,
)
