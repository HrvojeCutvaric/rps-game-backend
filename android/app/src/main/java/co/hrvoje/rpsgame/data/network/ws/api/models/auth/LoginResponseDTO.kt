package co.hrvoje.rpsgame.data.network.ws.api.models.auth

import com.google.gson.annotations.SerializedName

data class LoginResponseDTO(
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("username")
    val username: String?,
)
