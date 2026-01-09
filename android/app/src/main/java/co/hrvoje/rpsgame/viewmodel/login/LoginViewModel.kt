package co.hrvoje.rpsgame.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.hrvoje.rpsgame.R
import co.hrvoje.rpsgame.data.network.services.AuthService
import co.hrvoje.rpsgame.domain.utils.LoginThrowable
import co.hrvoje.rpsgame.navigation.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val appNavigator: AppNavigator,
    private val authService: AuthService,
) : ViewModel() {

    private val _state = MutableStateFlow(
        LoginState(
            username = "",
            password = "",
            isPasswordVisible = false,
            isButtonLoading = false,
            errorResource = null
        )
    )
    val state = _state.asStateFlow()

    fun execute(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClicked -> viewModelScope.launch {
                _state.update { it.copy(isButtonLoading = true) }
                authService.login(
                    username = state.value.username,
                    password = state.value.password,
                ).fold(
                    onSuccess = {
                        // TODO: Navigate to home screen
                        _state.update {
                            it.copy(
                                errorResource = null,
                                isButtonLoading = false,
                            )
                        }
                    },
                    onFailure = { error ->
                        val errorMessageResource = when (error) {
                            is LoginThrowable.IncorrectEmailPassword -> R.string.invalid_email_password
                            else -> R.string.generic_error_message
                        }
                        _state.update {
                            it.copy(
                                errorResource = errorMessageResource,
                                isButtonLoading = false,
                            )
                        }
                    }
                )
            }

            is LoginAction.OnPasswordChanged -> {
                _state.update { it.copy(password = action.password) }
            }

            LoginAction.OnPasswordVisibilityChanged -> {
                _state.update { it.copy(isPasswordVisible = _state.value.isPasswordVisible.not()) }
            }

            LoginAction.OnRegisterClicked -> {
                // TODO: Navigate to register screen
            }

            is LoginAction.OnUsernameChanged -> {
                _state.update { it.copy(username = action.username) }
            }
        }
    }
}
