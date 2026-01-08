package co.hrvoje.routing.models.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("user_id")
    val userId: String,
    @SerialName("username")
    val username: String
)
