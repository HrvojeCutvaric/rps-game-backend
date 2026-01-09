package co.hrvoje.rpsgame.view.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.hrvoje.rpsgame.R
import co.hrvoje.rpsgame.ui.components.DefaultButton
import co.hrvoje.rpsgame.ui.components.DefaultTextField
import co.hrvoje.rpsgame.ui.theme.RockPaperScissorsGameTheme
import co.hrvoje.rpsgame.viewmodel.register.RegisterAction
import co.hrvoje.rpsgame.viewmodel.register.RegisterState
import co.hrvoje.rpsgame.viewmodel.register.RegisterViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RegisterLayout(
        state = state,
        onAction = viewModel::execute,
    )
}

@Composable
private fun RegisterLayout(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.registration),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.height(36.dp)) {
            state.error?.let {
                Text(
                    text = stringResource(it),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.error,
                    ),
                )
            }
        }

        DefaultTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.username,
            onValueChange = { onAction(RegisterAction.OnUsernameChanged(it)) },
            label = R.string.username,
            placeholder = R.string.username,
        )

        Spacer(modifier = Modifier.height(12.dp))

        DefaultTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.password,
            onValueChange = { onAction(RegisterAction.OnPasswordChanged(it)) },
            label = R.string.password,
            placeholder = R.string.password,
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = if (state.isPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
            onTrailingIconClicked = { onAction(RegisterAction.OnPasswordVisibilityChanged) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        DefaultTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.confirmPassword,
            onValueChange = { onAction(RegisterAction.OnConfirmPasswordChanged(it)) },
            label = R.string.confirm_password,
            placeholder = R.string.confirm_password,
            visualTransformation = if (state.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = if (state.isConfirmPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
            onTrailingIconClicked = { onAction(RegisterAction.OnConfirmPasswordVisibilityChanged) },
        )

        Spacer(modifier = Modifier.height(24.dp))

        DefaultButton(
            modifier = Modifier.fillMaxWidth(),
            label = R.string.register,
            onButtonClicked = { onAction(RegisterAction.OnRegisterClicked) },
            isButtonLoading = state.isButtonLoading,
            isButtonEnabled = state.isButtonLoading.not(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.already_have_an_account),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.outline,
                )
            )

            TextButton(
                onClick = { onAction(RegisterAction.OnLoginClicked) }
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterLayoutPreview() {
    RockPaperScissorsGameTheme {
        RegisterLayout(
            state = RegisterState(
                password = "test123",
                confirmPassword = "test123",
                isPasswordVisible = false,
                isConfirmPasswordVisible = false,
                isButtonLoading = false,
                error = R.string.generic_error_message,
                username = "",
            ),
            onAction = {},
        )
    }
}
