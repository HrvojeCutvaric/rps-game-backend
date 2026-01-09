package co.hrvoje.rpsgame.viewmodel.login

data class LoginState(
    val username: String,
    val password: String,
    val isPasswordVisible: Boolean,
    val isButtonLoading: Boolean,
    val errorResource: Int?,
)
