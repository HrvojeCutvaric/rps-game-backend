package co.hrvoje.routing.models.register

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("username")
    val username: String,
)