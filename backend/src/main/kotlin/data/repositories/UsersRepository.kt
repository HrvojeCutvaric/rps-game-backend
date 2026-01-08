package co.hrvoje.data.repositories

import co.hrvoje.domain.models.User

interface UsersRepository {
    suspend fun create(username: String, password: String): User?

    suspend fun findByUsername(username: String): User?
}