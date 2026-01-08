package co.hrvoje.data.db

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5433/rps_db",
        driver = "org.postgresql.Driver",
        user = "rps_user",
        password = "rps_pass"
    )
}