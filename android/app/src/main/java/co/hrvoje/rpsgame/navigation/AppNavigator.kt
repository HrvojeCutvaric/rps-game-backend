package co.hrvoje.rpsgame.navigation

import android.util.Log
import androidx.compose.runtime.mutableStateListOf

class AppNavigator {

    val backStack = mutableStateListOf<Route>(Route.Login)

    fun navigateTo(
        route: Route,
        removeRoutes: List<Route>? = null,
    ) {
        removeRoutes?.let {
            backStack.removeAll(it)
        }
        backStack.add(route)
        Log.d("AppNavigator", "to backstack: $backStack")
    }

    fun navigateBack(
        route: Route? = null,
    ) {
        if (backStack.size <= 1) return
        route?.let {
            val index = backStack.indexOf(it)
            backStack.subList(index - 1, backStack.size).clear()
        } ?: backStack.removeLastOrNull()
        Log.d("AppNavigator", "back backstack: $backStack")
    }
}
