package co.hrvoje.rpsgame.data.network.ws.api

import co.hrvoje.rpsgame.data.network.ws.api.models.auth.LoginRequestBody
import co.hrvoje.rpsgame.data.network.ws.api.models.auth.LoginResponseDTO
import co.hrvoje.rpsgame.data.network.ws.api.models.auth.RegisterRequestBody
import co.hrvoje.rpsgame.data.network.ws.api.models.auth.RegisterResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {

    @POST("/auth/login")
    suspend fun login(
        @Body requestBody: LoginRequestBody,
    ): Response<LoginResponseDTO>

    @POST("/auth/register")
    suspend fun register(
        @Body requestBody: RegisterRequestBody,
    ): Response<RegisterResponseDTO>

}
