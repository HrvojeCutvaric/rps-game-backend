package co.hrvoje.rpsgame.domain.utils

sealed class RegisterThrowable : Throwable() {

    data object UsernameExists : RegisterThrowable() {
        private fun readResolve(): Any = UsernameExists
    }
}
