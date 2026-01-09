package co.hrvoje.rpsgame.viewmodel.register

import androidx.annotation.StringRes

data class RegisterState(
    val username: String,
    val password: String,
    val confirmPassword: String,
    val isPasswordVisible: Boolean,
    val isConfirmPasswordVisible: Boolean,
    @StringRes val error: Int?,
    val isButtonLoading: Boolean,
)
