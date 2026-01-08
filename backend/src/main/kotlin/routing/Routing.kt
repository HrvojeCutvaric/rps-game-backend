package co.hrvoje.routing

import co.hrvoje.data.repositories.UsersRepository
import co.hrvoje.routing.models.error.ErrorResponse
import co.hrvoje.routing.models.login.LoginRequest
import co.hrvoje.routing.models.login.LoginResponse
import co.hrvoje.routing.models.register.RegisterRequest
import co.hrvoje.routing.models.register.RegisterResponse
import co.hrvoje.utils.HashingManager
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    usersRepository: UsersRepository,
    hashingManager: HashingManager,
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
        }
    }
}