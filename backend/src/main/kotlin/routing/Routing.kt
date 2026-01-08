package co.hrvoje.routing

import co.hrvoje.data.repositories.GamePlayersRepository
import co.hrvoje.data.repositories.GamesRepository
import co.hrvoje.data.repositories.UsersRepository
import co.hrvoje.domain.models.GamePlayer
import co.hrvoje.domain.utils.GameState
import co.hrvoje.routing.models.error.ErrorResponse
import co.hrvoje.routing.models.games.create.CreateGameRequest
import co.hrvoje.routing.models.games.create.CreateGameResponse
import co.hrvoje.routing.models.join.JoinGameRequest
import co.hrvoje.routing.models.login.LoginRequest
import co.hrvoje.routing.models.login.LoginResponse
import co.hrvoje.routing.models.logout.LogoutResponse
import co.hrvoje.routing.models.register.RegisterRequest
import co.hrvoje.routing.models.register.RegisterResponse
import co.hrvoje.utils.HashingManager
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    usersRepository: UsersRepository,
    hashingManager: HashingManager,
    gamesRepository: GamesRepository,
    gamePlayersRepository: GamePlayersRepository,
) {
    routing {
        get("/health") {
            call.respond(
                status = HttpStatusCode.OK,
                message = mapOf(
                    "status" to "UP",
                    "message" to "Backend is running"
                )
            )
        }
        route("/auth") {
            post("/register") {
                try {
                    val registerRequest = call.receive<RegisterRequest>()
                    val hashedPassword = hashingManager.hash(plain = registerRequest.password)
                    val createdUser = usersRepository.create(
                        username = registerRequest.username,
                        password = hashedPassword
                    )
                    createdUser?.let {
                        call.respond<RegisterResponse>(
                            status = HttpStatusCode.Created,
                            message = RegisterResponse(
                                id = createdUser.id,
                                username = createdUser.username
                            )
                        )
                    } ?: run {
                        println("Failed to create user")
                        call.respond<ErrorResponse>(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse(message = "Failed to create user")
                        )
                    }
                } catch (ex: IllegalStateException) {
                    println(ex.message)
                    call.respond<ErrorResponse>(
                        status = HttpStatusCode.BadRequest,
                        message = ErrorResponse(message = "Invalid request state")
                    )
                } catch (ex: JsonConvertException) {
                    println(ex.message)
                    call.respond<ErrorResponse>(
                        status = HttpStatusCode.BadRequest,
                        message = ErrorResponse(message = "Request body is not valid JSON")
                    )
                } catch (ex: Exception) {
                    println(ex.message)
                    call.respond<ErrorResponse>(
                        status = HttpStatusCode.BadRequest,
                        message = ErrorResponse(message = "Bad request data")
                    )
                }
            }

            post("/login") {
                try {
                    val request = call.receive<LoginRequest>()
                    val user = usersRepository.findByUsername(request.username)
                        ?: run {
                            call.respond<ErrorResponse>(
                                status = HttpStatusCode.Unauthorized,
                                message = ErrorResponse(
                                    message = "Invalid username or password"
                                )
                            )
                            return@post
                        }

                    val passwordValid = hashingManager.verify(
                        request.password,
                        user.password
                    )

                    if (!passwordValid) {
                        call.respond<ErrorResponse>(
                            status = HttpStatusCode.Unauthorized,
                            message = ErrorResponse(
                                message = "Invalid username or password"
                            )
                        )
                        return@post
                    }

                    call.respond<LoginResponse>(
                        status = HttpStatusCode.OK,
                        message = LoginResponse(
                            userId = user.id.toString(),
                            username = user.username
                        )
                    )
                } catch (ex: Exception) {
                    println(ex.message)
                    call.respond<ErrorResponse>(
                        status = HttpStatusCode.BadRequest,
                        message = ErrorResponse(message = "Bad request data")
                    )
                }
            }

            post("/logout") {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = LogoutResponse(
                        status = "OK",
                        message = "User logged out"
                    )
                )
            }
        }

        route("/games") {
            get {
                val stateParam = call.request.queryParameters["state"]
                val stateFilter = stateParam?.let {
                    try {
                        GameState.valueOf(it)
                    } catch (e: IllegalArgumentException) {
                        null
                    }
                }

                val games = gamesRepository.getGames(stateFilter)

                call.respond(games)
            }

            post {
                try {
                    val request = call.receive<CreateGameRequest>()
                    val user = usersRepository.findByUsername(request.username)
                    if (user == null) {
                        call.respond<ErrorResponse>(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse(message = "User not found")
                        )
                        return@post
                    }

                    val createdGame = gamesRepository.create()

                    if (createdGame == null) {
                        call.respond<ErrorResponse>(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse(message = "Game could not be created")
                        )
                        return@post
                    }

                    val createdGamePlayer = gamePlayersRepository.create(
                        GamePlayer(
                            user = user,
                            game = createdGame,
                            score = 0,
                            hasCreatedGamme = true,
                        )
                    )

                    if (createdGamePlayer == null) {
                        call.respond<ErrorResponse>(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse(message = "Game could not be created")
                        )
                        return@post
                    }

                    call.respond<CreateGameResponse>(
                        status = HttpStatusCode.Created,
                        message = CreateGameResponse(game = createdGame)
                    )
                } catch (ex: Exception) {
                    println(ex.message)
                    call.respond<ErrorResponse>(
                        status = HttpStatusCode.BadRequest,
                        message = ErrorResponse(message = "Bad request data")
                    )
                }
            }

            post("/{gameId}/join") {
                try {
                    val gameIdParam = call.parameters["gameId"]?.toIntOrNull()
                    if (gameIdParam == null) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            ErrorResponse("Invalid game ID")
                        )
                        return@post
                    }

                    val request = call.receive<JoinGameRequest>()

                    val user = usersRepository.findByUsername(request.username)
                    if (user == null) {
                        call.respond<ErrorResponse>(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse(message = "User not found")
                        )
                        return@post
                    }

                    val game = gamesRepository.getGameById(id = gameIdParam)
                    if (game == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse("Invalid game ID")
                        )
                        return@post
                    }

                    if (game.state != GameState.WAITING_FOR_PLAYERS) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse(message = "Game already has player")
                        )
                    }

                    val createdGamePlayer = gamePlayersRepository.create(
                        gamePlayer = GamePlayer(
                            user = user,
                            game = game,
                            score = 0,
                            hasCreatedGamme = false,
                        )
                    )

                    if (createdGamePlayer == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse(message = "Failed to join game")
                        )
                    }

                    val updatedGame = gamesRepository.update(game.copy(state = GameState.IN_PROGRESS))

                    if (updatedGame == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse(message = "Game could not be updated")
                        )
                        return@post
                    }

                    call.respond(
                        status = HttpStatusCode.OK,
                        message = mapOf("status" to "OK"),
                    )

                } catch (ex: Exception) {
                    println(ex.message)
                    call.respond<ErrorResponse>(
                        status = HttpStatusCode.BadRequest,
                        message = ErrorResponse(message = "Bad request data")
                    )
                }
            }
        }
    }
}