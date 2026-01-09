package co.hrvoje.rpsgame.data.network.ws

import android.util.Log
import co.hrvoje.rpsgame.data.network.services.AuthService
import co.hrvoje.rpsgame.data.network.ws.api.AuthAPI
import co.hrvoje.rpsgame.data.network.ws.api.mappers.toLoginResponse
import co.hrvoje.rpsgame.data.network.ws.api.models.auth.LoginRequestBody
import co.hrvoje.rpsgame.data.network.ws.api.models.auth.RegisterRequestBody
import co.hrvoje.rpsgame.domain.models.LoginResponse
import co.hrvoje.rpsgame.domain.utils.LoginThrowable
import co.hrvoje.rpsgame.domain.utils.RegisterThrowable

class WSAuthService(
    private val authAPI: AuthAPI,
) : AuthService {

    override suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponse> {
        val loginResult = try {
            authAPI.login(
                requestBody = LoginRequestBody(
                    username = username,
                    password = password
                )
            )
        } catch (error: Exception) {
            Log.e("WSAuthService", "login: ", error)
            return Result.failure(LoginThrowable.Generic)
        }

        if (!loginResult.isSuccessful) {
            return when (loginResult.code()) {
                400, 401 -> Result.failure(LoginThrowable.IncorrectEmailPassword)
                else -> Result.failure(LoginThrowable.Generic)
            }
        }

        val loginResponseDto =
            loginResult.body() ?: return Result.failure(LoginThrowable.Generic)


        return Result.success(loginResponseDto.toLoginResponse())
    }

    override suspend fun register(
        username: String,
        password: String
    ): Result<Unit> {
        try {
            val registerResult = authAPI.register(
                requestBody = RegisterRequestBody(
                    username = username,
                    password = password,
                )
            )

            if (registerResult.isSuccessful.not()) {
                return Result.failure(RegisterThrowable.UsernameExists)
            }

            return Result.success(Unit)
        } catch (error: Throwable) {
            Log.e("WSAuthService", "register: ", error)
            return Result.failure(Throwable("Something went wrong"))
        }
    }
}
