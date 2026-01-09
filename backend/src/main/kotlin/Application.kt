package co.hrvoje

import co.hrvoje.data.db.configureDatabases
import co.hrvoje.data.repositories.GamesRepositoryImpl
import co.hrvoje.data.repositories.RoundsRepositoryImpl
import co.hrvoje.data.repositories.UsersRepositoryImpl
import co.hrvoje.routing.configureRouting
import co.hrvoje.utils.HashingManagerImpl
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val usersRepository = UsersRepositoryImpl()
    val hashingManager = HashingManagerImpl()
    val gamesRepository = GamesRepositoryImpl()
    val roundsRepository = RoundsRepositoryImpl()

    install(ContentNegotiation) {
        json()
    }
    configureDatabases()
    configureRouting(
        usersRepository = usersRepository,
        hashingManager = hashingManager,
        gamesRepository = gamesRepository,
        roundsRepository = roundsRepository,
    )
}
