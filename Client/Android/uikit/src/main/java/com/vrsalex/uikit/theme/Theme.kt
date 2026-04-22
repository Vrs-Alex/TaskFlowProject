package com.vrsalex.uikit.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

@Immutable
data class AppColors(
    val primary: Color,
    val onPrimary: Color,

    val secondary: Color,
    val onSecondary: Color,

    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,

    val onSurface: Color,
    val onSurfaceVariant: Color,

    val success: Color,
    val warning: Color,
    val error: Color,

    val pureSurface: Color
)

@Immutable
data class AppTypes(
    val displayLarge: TextStyle, // splash screen
    val headline: TextStyle, // screen header
    val title: TextStyle, // card name
    val body: TextStyle, // base text
    val bodyMedium: TextStyle, // second text
    val label: TextStyle, // cheap, helper
    val button: TextStyle, // btn
)

@Immutable
data class AppShapes(
    val small: Shape, // tag, btn
    val medium: Shape, // item card, text input
    val large: Shape, // bottom sheet, dialog
    val extraLarge: Shape // big container
)


val LocalAppColors = staticCompositionLocalOf {
    AppColors(
        primary = Color.Unspecified,
        onPrimary = Color.Unspecified,
        secondary = Color.Unspecified,
        onSecondary = Color.Unspecified,
        background = Color.Unspecified,
        surface = Color.Unspecified,
        surfaceVariant = Color.Unspecified,
        onSurface = Color.Unspecified,
        onSurfaceVariant = Color.Unspecified,
        success = Color.Unspecified,
        warning = Color.Unspecified,
        error = Color.Unspecified,
        pureSurface = Color.Unspecified
    )
}

val LocalAppTypes = staticCompositionLocalOf {
    AppTypes(
        displayLarge = TextStyle.Default,
        headline = TextStyle.Default,
        title = TextStyle.Default,
        body = TextStyle.Default,
        bodyMedium = TextStyle.Default,
        label = TextStyle.Default,
        button = TextStyle.Default
    )
}

val LocalAppShapes = staticCompositionLocalOf {
    AppShapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(20.dp),
        extraLarge = RoundedCornerShape(24.dp)
    )
}


@Composable
fun TaskFlowTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
){


    val view = LocalView.current
    DisposableEffect(isDarkTheme) {
        val window = (view.context as? Activity)?.window
        if (window != null) {
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !isDarkTheme
            controller.isAppearanceLightNavigationBars = !isDarkTheme
        }

        onDispose {}
    }

    val colors = if (isDarkTheme) darkAppColors else darkAppColors

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypes provides onestAppTypes,
        LocalAppShapes provides shapes,
        content = content
    )
}


object AppTheme {
    val colors: AppColors @Composable get() =
        LocalAppColors.current
    val types: AppTypes @Composable get() =
        LocalAppTypes.current
    val shapes: AppShapes @Composable get() =
        LocalAppShapes.current
}


private val shapes = AppShapes (
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

//
//val lightAppColors = AppColors(
//        primary = PrimaryLight,
//        onPrimary = OnPrimaryLight,
//        secondary = SecondaryLight,
//        onSecondary = OnSecondaryLight,
//        background = BackgroundLight,
//        surface = SurfaceLight,
//        surfaceVariant = SurfaceVariantLight,
//        onSurface = OnSurfaceLight,
//        onSurfaceVariant = OnSurfaceVariantLight,
//        success = SuccessLight,
//        warning = WarningLight,
//        error = ErrorLight,
//        pureSurface = Color.White
//    )

val darkAppColors = AppColors(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurface = OnSurfaceDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    success = SuccessDark,
    warning = WarningDark,
    error = ErrorDark,
    pureSurface = Color.Black
)


private val onestAppTypes = AppTypes(

    displayLarge = TextStyle(
        fontFamily = onestFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 56.sp,
        letterSpacing = (-0.2).sp
    ),

    headline = TextStyle(
        fontFamily = onestFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),

    title = TextStyle(
        fontFamily = onestFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.15.sp
    ),

    body = TextStyle(
        fontFamily = onestFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = onestFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),

    label = TextStyle(
        fontFamily = onestFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.5.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    ),

    button = TextStyle(
        fontFamily = onestFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
)

