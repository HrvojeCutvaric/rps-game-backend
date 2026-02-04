package co.hrvoje.drawshapes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DrawShapesTheme(
    content: @Composable (PaddingValues) -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AndroidTypography(),
        content = {
            Scaffold(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .background(color = MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(WindowInsets.statusBars),
                content = content
            )
        }
    )
}
