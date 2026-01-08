package co.hrvoje.routing.models.logout

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LogoutResponse(
    @SerialName("status")
    val status: String,
    @SerialName("message")
    val message: String
)
