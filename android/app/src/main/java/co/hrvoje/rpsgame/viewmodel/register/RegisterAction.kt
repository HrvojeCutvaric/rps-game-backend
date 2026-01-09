package co.hrvoje.rpsgame.viewmodel.register

sealed interface RegisterAction {

    data class OnUsernameChanged(val username: String) : RegisterAction

    data class OnPasswordChanged(val newPassword: String) : RegisterAction

    data class OnConfirmPasswordChanged(val newConfirmPassword: String) : RegisterAction

    data object OnPasswordVisibilityChanged : RegisterAction

    data object OnConfirmPasswordVisibilityChanged : RegisterAction

    data object OnRegisterClicked : RegisterAction

    data object OnLoginClicked : RegisterAction
}
