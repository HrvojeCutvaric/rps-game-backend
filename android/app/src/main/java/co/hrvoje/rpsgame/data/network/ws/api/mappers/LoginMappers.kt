package co.hrvoje.rpsgame.data.network.ws.api.mappers

import co.hrvoje.rpsgame.data.network.ws.api.models.auth.LoginResponseDTO
import co.hrvoje.rpsgame.domain.models.LoginResponse

fun LoginResponseDTO.toLoginResponse(): LoginResponse =
    LoginResponse(
        userId = userId ?: "",
        username = username.orEmpty(),
    )
