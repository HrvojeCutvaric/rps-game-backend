package co.hrvoje.drawshapes.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun AndroidTypography(): Typography {
    return Typography(
        titleLarge = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
        ),
    )
}
