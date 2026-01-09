package co.hrvoje.rpsgame.data.network.services

import co.hrvoje.rpsgame.domain.models.LoginResponse

interface AuthService {

    suspend fun login(username: String, password: String): Result<LoginResponse>
}
