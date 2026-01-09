package co.hrvoje.rpsgame.data.network.ws.api.models.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequestBody(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
)
