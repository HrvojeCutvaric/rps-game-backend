package co.hrvoje.routing.models.register

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String,
)