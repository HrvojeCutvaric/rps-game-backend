package co.hrvoje.routing.models.error

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("message")
    val message: String
)
