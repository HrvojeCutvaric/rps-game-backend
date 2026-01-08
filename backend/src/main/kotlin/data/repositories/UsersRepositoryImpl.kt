package co.hrvoje.data.repositories

import co.hrvoje.data.db.dao.UserDAO
import co.hrvoje.data.db.mappers.suspendTransaction
import co.hrvoje.data.db.mappers.toUser
import co.hrvoje.domain.models.User

class UsersRepositoryImpl : UsersRepository {
    override suspend fun create(username: String, password: String): User? = suspendTransaction {
        try {
            UserDAO.new {
                this.username = username
                this.password = password
            }.toUser()
        } catch (error: Throwable) {
            println("Error while creating user: ${error.message}")
            null
        }
    }
}