package co.hrvoje.rpsgame.data.network.ws.api.models.auth

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestBody(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)
