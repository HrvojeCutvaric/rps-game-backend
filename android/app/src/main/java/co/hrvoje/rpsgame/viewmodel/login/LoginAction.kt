package co.hrvoje.rpsgame.viewmodel.login

sealed interface LoginAction {
    data class OnUsernameChanged(val username: String) : LoginAction
    data class OnPasswordChanged(val password: String) : LoginAction
    data object OnPasswordVisibilityChanged : LoginAction
    data object OnLoginClicked : LoginAction
    data object OnRegisterClicked : LoginAction
}
