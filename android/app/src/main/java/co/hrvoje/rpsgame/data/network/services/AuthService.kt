package co.hrvoje.rpsgame.data.network.services

import co.hrvoje.rpsgame.domain.models.LoginResponse
import co.hrvoje.rpsgame.domain.models.User

interface AuthService {

    suspend fun login(username: String, password: String): Result<User>

    suspend fun register(username: String, password: String): Result<Unit>
}
