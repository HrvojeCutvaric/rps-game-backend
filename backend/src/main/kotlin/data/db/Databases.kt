package co.hrvoje.data.db

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    val dbHost = System.getenv("DB_HOST")
    val dbPort = System.getenv("DB_PORT")
    val dbName = System.getenv("DB_NAME")
    val dbUser = System.getenv("DB_USER")
    val dbPassword = System.getenv("DB_PASSWORD")

    Database.connect(
        url = "jdbc:postgresql://$dbHost:$dbPort/$dbName",
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )
}