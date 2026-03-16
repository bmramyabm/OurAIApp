package com.example.ouraiapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Mint,
    onPrimary = Night,
    secondary = Sky,
    tertiary = MintDeep,
    background = Night,
    onBackground = Color.White,
    surface = NightSurface,
    onSurface = Color.White,
    surfaceVariant = NightSurfaceVariant,
    onSurfaceVariant = TextMuted,
    outline = TextMuted.copy(alpha = 0.45f)
)

@Composable
fun OurAiAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
