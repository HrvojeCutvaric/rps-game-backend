package co.hrvoje.rpsgame.data.network.ws.api.models.auth

import com.google.gson.annotations.SerializedName

data class RegisterResponseDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
)
