package co.hrvoje.rpsgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import co.hrvoje.rpsgame.navigation.AppNavigator
import co.hrvoje.rpsgame.navigation.Route
import co.hrvoje.rpsgame.ui.theme.RockPaperScissorsGameTheme
import co.hrvoje.rpsgame.view.games.GamesScreen
import co.hrvoje.rpsgame.view.login.LoginScreen
import co.hrvoje.rpsgame.view.register.RegisterScreen
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val appNavigator = koinInject<AppNavigator>()
            RockPaperScissorsGameTheme {
                NavDisplay(
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    backStack = appNavigator.backStack,
                    transitionSpec = {
                        fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                    },
                    entryProvider = entryProvider {
                        entry<Route.Login> {
                            LoginScreen()
                        }
                        entry<Route.Register> {
                            RegisterScreen()
                        }
                        entry<Route.Games> {
                            GamesScreen()
                        }
                    },
                )
            }
        }
    }
}
