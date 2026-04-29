package com.vrsalex.uikit.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

@Immutable
data class AppColors(
    val primary: Color,
    val onPrimary: Color,
    val primarySoft: Color,
    val primaryBorder: Color,

    val secondary: Color,
    val onSecondary: Color,

    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val surfaceElevated: Color,

    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,
    val onSurfaceMuted: Color,

    val outline: Color,
    val outlineVariant: Color,

    val success: Color,
    val warning: Color,
    val error: Color,

    val scrim: Color,
)


@Immutable
data class AppTypeColors(
    val event: Color,       val eventSoft: Color,   val eventBorder: Color,
    val task: Color,        val taskSoft: Color,    val taskBorder: Color,
    val goal: Color,        val goalSoft: Color,    val goalBorder: Color,
    val habit: Color,       val habitSoft: Color,   val habitBorder: Color,
)

@Immutable
data class AppTypes(
    val displayLarge: TextStyle,   // splash screen — "TaskFlow"
    val displayMedium: TextStyle,  // крупные числа метрик (streak, сумма)
    val headline: TextStyle,       // заголовок экрана ("Сегодня, 24 апреля")
    val title: TextStyle,          // название карточки
    val titleLarge: TextStyle,     // заголовок bottom sheet
    val body: TextStyle,           // основной текст (описание заметки)
    val bodyMedium: TextStyle,     // второстепенный текст (meta строка карточки)
    val label: TextStyle,          // чипы, helper, подписи в инпутах
    val caption: TextStyle,        // UPPERCASE подписи секций, overline
    val micro: TextStyle,          // 10sp для бейджей/дней недели
    val button: TextStyle,         // primary/secondary кнопка
)

@Immutable
data class AppShapes(
    val small: Shape,         // chip, segmented item (8.dp)
    val medium: Shape,        // card, input, bottom nav icon (12.dp)
    val large: Shape,         // FAB, большая карточка-метрика (16.dp)
    val extraLarge: Shape,    // bottom sheet (topStart/topEnd 22.dp)
    val round: Shape,         // pills, статус-чипы, аватар (CircleShape)
)

val darkAppColors = AppColors(
    primary          = PrimaryDark,
    onPrimary        = OnPrimaryDark,
    primarySoft      = PrimarySoftDark,
    primaryBorder    = PrimaryBorderDark,
    secondary        = SecondaryDark,
    onSecondary      = OnSecondaryDark,
    background       = BackgroundDark,
    surface          = SurfaceDark,
    surfaceVariant   = SurfaceVariantDark,
    surfaceElevated  = SurfaceElevatedDark,
    onBackground     = OnBackgroundDark,
    onSurface        = OnSurfaceDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    onSurfaceMuted   = OnSurfaceMutedDark,
    outline          = OutlineDark,
    outlineVariant   = OutlineVariantDark,
    success          = SuccessDark,
    warning          = WarningDark,
    error            = ErrorDark,
    scrim            = ScrimDark
)

val darkTypeColors = AppTypeColors(
    event = EventHue, eventSoft = EventSoft, eventBorder = EventBorder,
    task  = TaskHue,  taskSoft  = TaskSoft,  taskBorder  = TaskBorder,
    goal  = GoalHue,  goalSoft  = GoalSoft,  goalBorder  = GoalBorder,
    habit = HabitHue, habitSoft = HabitSoft, habitBorder = HabitBorder,
)

private val onestAppTypes = AppTypes(
    displayLarge = TextStyle(
        fontFamily = onestFontFamily, fontWeight = FontWeight.ExtraBold,
        fontSize = 48.sp, lineHeight = 56.sp, letterSpacing = (-0.4).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = onestFontFamily, fontWeight = FontWeight.Bold,
        fontSize = 28.sp, lineHeight = 32.sp, letterSpacing = (-0.2).sp,
    ),
    headline = TextStyle(
        fontFamily = onestFontFamily, fontWeight = FontWeight.Bold,
        fontSize = 26.sp, lineHeight = 30.sp, letterSpacing = (-0.2).sp,
    ),
    title = TextStyle(
        fontFamily = onestFontFamily, fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = onestFontFamily, fontWeight = FontWeight.Bold,
        fontSize = 21.sp, lineHeight = 24.sp, letterSpacing = (-0.1).sp,
    ),
    body = TextStyle(
        fontFamily = onestFontFamily, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 22.sp, letterSpacing = 0.15.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = onestFontFamily, fontWeight = FontWeight.Medium,
        fontSize = 15.sp, lineHeight = 18.sp, letterSpacing = 0.2.sp,
    ),
    label = TextStyle(
        fontFamily = onestFontFamily, fontWeight = FontWeight.Medium,
        fontSize = 13.5.sp, lineHeight = 16.sp, letterSpacing = 0.3.sp,
    ),
    caption = TextStyle(
        fontFamily = onestFontFamily, fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp, lineHeight = 14.sp, letterSpacing = 0.6.sp,
    ),
    micro = TextStyle(
        fontFamily = onestFontFamily, fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp, lineHeight = 12.sp, letterSpacing = 0.4.sp,
    ),
    button = TextStyle(
        fontFamily = onestFontFamily, fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp, lineHeight = 18.sp, letterSpacing = 0.3.sp,
    ),
)

private val shapes = AppShapes(
    small      = RoundedCornerShape(8.dp),
    medium     = RoundedCornerShape(12.dp),
    large      = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
    round      = CircleShape,
)


val LocalAppColors = staticCompositionLocalOf { darkAppColors }
val LocalAppTypeColors = staticCompositionLocalOf { darkTypeColors }
val LocalAppTypes = staticCompositionLocalOf { onestAppTypes }
val LocalAppShapes = staticCompositionLocalOf { shapes }

object AppTheme {
    val colors: AppColors          @Composable get() = LocalAppColors.current
    val typeColors: AppTypeColors  @Composable get() = LocalAppTypeColors.current
    val types: AppTypes            @Composable get() = LocalAppTypes.current
    val shapes: AppShapes          @Composable get() = LocalAppShapes.current
}

@Composable
fun TaskFlowTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
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

    val colors = darkAppColors

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypeColors provides darkTypeColors,
        LocalAppTypes provides onestAppTypes,
        LocalAppShapes provides shapes,
        content = content,
    )
}