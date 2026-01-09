package co.hrvoje.rpsgame.viewmodel.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.hrvoje.rpsgame.R
import co.hrvoje.rpsgame.data.network.services.AuthService
import co.hrvoje.rpsgame.domain.utils.RegisterThrowable
import co.hrvoje.rpsgame.navigation.AppNavigator
import co.hrvoje.rpsgame.navigation.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authService: AuthService,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private val _state = MutableStateFlow(
        RegisterState(
            username = "",
            password = "",
            confirmPassword = "",
            isPasswordVisible = false,
            isConfirmPasswordVisible = false,
            isButtonLoading = false,
            error = null,
        )
    )
    val state = _state.asStateFlow()

    fun execute(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnConfirmPasswordChanged -> {
                _state.update {
                    it.copy(confirmPassword = action.newConfirmPassword)
                }
            }

            RegisterAction.OnConfirmPasswordVisibilityChanged -> {
                _state.update {
                    it.copy(isConfirmPasswordVisible = it.isConfirmPasswordVisible.not())
                }
            }

            is RegisterAction.OnPasswordChanged -> {
                _state.update {
                    it.copy(password = action.newPassword)
                }
            }

            RegisterAction.OnPasswordVisibilityChanged -> {
                _state.update {
                    it.copy(isPasswordVisible = it.isPasswordVisible.not())
                }
            }

            RegisterAction.OnRegisterClicked -> viewModelScope.launch(Dispatchers.IO) {
                _state.update {
                    it.copy(isButtonLoading = true)
                }

                val currentState = _state.value

                if (currentState.password != currentState.confirmPassword) {
                    _state.update {
                        it.copy(
                            isButtonLoading = false,
                            error = R.string.confirm_password_not_match,
                        )
                    }
                    return@launch
                }

                authService.register(
                    username = currentState.username,
                    password = currentState.password,
                ).fold(
                    onSuccess = {
                        _state.update {
                            it.copy(
                                isButtonLoading = false,
                                error = null,
                            )
                        }
                        appNavigator.navigateTo(
                            route = Route.Login,
                            removeRoutes = listOf(Route.Register),
                        )
                    },
                    onFailure = { throwable ->
                        _state.update {
                            it.copy(
                                isButtonLoading = false,
                                error = when (throwable) {
                                    RegisterThrowable.UsernameExists -> R.string.username_exists
                                    else -> R.string.generic_error_message
                                }
                            )
                        }
                    }
                )
            }

            RegisterAction.OnLoginClicked -> viewModelScope.launch {
                appNavigator.navigateTo(
                    route = Route.Login,
                    removeRoutes = listOf(Route.Register),
                )
            }

            is RegisterAction.OnUsernameChanged -> {
                _state.update { it.copy(username = action.username) }
            }
        }
    }
}
