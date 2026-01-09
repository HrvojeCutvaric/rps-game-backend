package co.hrvoje.rpsgame.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.hrvoje.rpsgame.navigation.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val appNavigator: AppNavigator,
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
                // TODO: Login user
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
