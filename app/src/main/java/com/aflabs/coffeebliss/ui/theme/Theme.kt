package com.aflabs.coffeebliss.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val CoffeePrimary = Color(0xFF4E342E)     // Deep warm brown
val CoffeeSecondary = Color(0xFF8D6E63)   // Creamy latte brown
val CoffeeTertiary = Color(0xFFFFB300)    // Gold/honey accent
val CoffeeBackground = Color(0xFFFAF6F0)  // Warm sandy white
val CoffeeSurface = Color(0xFFFFFFFF)     // Clean white
val CoffeeOnPrimary = Color(0xFFFFFFFF)
val CoffeeOnBackground = Color(0xFF2E1C16) // Espresso black text
val CoffeeOnSurface = Color(0xFF2E1C16)

private val LightColorScheme = lightColorScheme(
    primary = CoffeePrimary,
    secondary = CoffeeSecondary,
    tertiary = CoffeeTertiary,
    background = CoffeeBackground,
    surface = CoffeeSurface,
    onPrimary = CoffeeOnPrimary,
    onBackground = CoffeeOnBackground,
    onSurface = CoffeeOnSurface
)

@Composable
fun CoffeeBlissTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
