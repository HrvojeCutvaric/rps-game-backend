package co.hrvoje.data.db.mappers

import co.hrvoje.data.db.dao.UserDAO
import co.hrvoje.domain.models.User
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun UserDAO.toUser() = User(
    id = this.id.value,
    username = this.username,
    password = this.password,
)