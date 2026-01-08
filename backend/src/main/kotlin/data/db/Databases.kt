package co.hrvoje.data.db

import co.hrvoje.data.db.tables.Users
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val database =  Database.connect(
        url = "jdbc:postgresql://localhost:5433/rps_db",
        driver = "org.postgresql.Driver",
        user = "rps_user",
        password = "rps_pass"
    )
    transaction (database) {
        SchemaUtils.create(Users)
    }
}