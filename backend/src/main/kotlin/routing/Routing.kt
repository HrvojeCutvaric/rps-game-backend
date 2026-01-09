package co.hrvoje.routing

import co.hrvoje.data.repositories.*
import co.hrvoje.domain.models.GamePlayer
import co.hrvoje.domain.utils.GameState
import co.hrvoje.domain.utils.MoveType
import co.hrvoje.routing.models.create_move.CreateMoveRequest
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
    roundsRepository: RoundsRepository,
    movesRepository: MovesRepository,
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
                        return@post
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

            post("/{gameId}/rounds") {
                try {
                    val gameId = call.parameters["gameId"]?.toIntOrNull()
                        ?: throw BadRequestException("Invalid game id")

                    val game = gamesRepository.getGameById(id = gameId)
                    if (game == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse("Invalid game ID")
                        )
                        return@post
                    }

                    if (game.state != GameState.IN_PROGRESS) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse(message = "Game is not in progress")
                        )
                        return@post
                    }

                    val round = roundsRepository.create(gameId)

                    if (round == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse("Invalid round")
                        )
                        return@post
                    }

                    call.respond(status = HttpStatusCode.Created, message = round)
                } catch (ex: BadRequestException) {
                    println(ex.message)
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse(ex.message ?: "Bad request"))
                } catch (ex: Exception) {
                    println(ex.message)
                    call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Failed to create round"))
                }
            }

            post("/{gameId}/rounds/{roundId}/moves") {
                try {
                    val roundId = call.parameters["roundId"]?.toIntOrNull()
                        ?: throw BadRequestException("Invalid round id")

                    val round = roundsRepository.getRoundById(roundId)

                    if (round == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse("Invalid round")
                        )
                        return@post
                    }

                    val request = call.receive<CreateMoveRequest>()

                    val user = usersRepository.findByUsername(request.username)
                    if (user == null) {
                        call.respond<ErrorResponse>(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse(message = "User not found")
                        )
                        return@post
                    }

                    val choice = try {
                        MoveType.valueOf(request.choice)
                    } catch (e: IllegalArgumentException) {
                        null
                    }

                    if (choice == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse(message = "Bad choice")
                        )
                        return@post
                    }

                    val move = movesRepository.createMove(
                        user = user,
                        round = round,
                        choice = choice
                    )
                    if (move == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = ErrorResponse(message = "Failed to create move for round")
                        )
                        return@post
                    }
                    call.respond(
                        move.copy(user = user.copy(password = ""))
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