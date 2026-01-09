package co.hrvoje.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Game(
    @SerialName("id")
    val id: Int,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("first_user")
    val firstUser: User,
    @SerialName("second_user")
    val secondUser: User?,
)
