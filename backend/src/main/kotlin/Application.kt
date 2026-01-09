package co.hrvoje

import co.hrvoje.data.db.configureDatabases
import co.hrvoje.data.repositories.*
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
    val gamePlayersRepository = GamePlayersRepositoryImpl()
    val roundsRepository = RoundsRepositoryImpl()
    val movesRepository = MovesRepositoryImpl()

    install(ContentNegotiation) {
        json()
    }
    configureDatabases()
    configureRouting(
        usersRepository = usersRepository,
        hashingManager = hashingManager,
        gamesRepository = gamesRepository,
        gamePlayersRepository = gamePlayersRepository,
        roundsRepository = roundsRepository,
        movesRepository = movesRepository,
    )
}
