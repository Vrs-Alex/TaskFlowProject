package com.vrsalex.taskflow


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────
//  DESIGN TOKENS
// ─────────────────────────────────────────────

object AppColors {
    val Primary       = Color(0xFF6C63FF)
    val PrimaryDark   = Color(0xFF4B44CC)
    val PrimaryLight  = Color(0xFFEDECFF)
    val Secondary     = Color(0xFF03DAC6)
    val Error         = Color(0xFFE53935)
    val Warning       = Color(0xFFFFA726)
    val Success       = Color(0xFF43A047)
    val Info          = Color(0xFF1E88E5)

    val Background    = Color(0xFFF8F8FC)
    val Surface       = Color(0xFFFFFFFF)
    val SurfaceVariant= Color(0xFFF1F0FA)
    val OnSurface     = Color(0xFF1A1A2E)
    val OnSurfaceVariant = Color(0xFF6B6B80)
    val Outline       = Color(0xFFD1D0E0)
    val OutlineFocus  = Color(0xFF6C63FF)

    val TextPrimary   = Color(0xFF1A1A2E)
    val TextSecondary = Color(0xFF6B6B80)
    val TextDisabled  = Color(0xFFB0B0C0)
    val TextOnPrimary = Color(0xFFFFFFFF)
}

object AppShapes {
    val ExtraSmall = RoundedCornerShape(4.dp)
    val Small      = RoundedCornerShape(8.dp)
    val Medium     = RoundedCornerShape(12.dp)
    val Large      = RoundedCornerShape(16.dp)
    val ExtraLarge = RoundedCornerShape(24.dp)
    val Circle     = CircleShape
}

object AppSpacing {
    val XXS = 2.dp
    val XS  = 4.dp
    val S   = 8.dp
    val M   = 12.dp
    val L   = 16.dp
    val XL  = 24.dp
    val XXL = 32.dp
}

object AppElevation {
    val None   = 0.dp
    val Small  = 2.dp
    val Medium = 4.dp
    val Large  = 8.dp
}

// ─────────────────────────────────────────────
//  BUTTONS
// ─────────────────────────────────────────────

enum class AppButtonVariant { Primary, Secondary, Outlined, Ghost, Danger }
enum class AppButtonSize    { Small, Medium, Large }

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AppButtonVariant = AppButtonVariant.Primary,
    size: AppButtonSize = AppButtonSize.Medium,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val height: Dp = when (size) {
        AppButtonSize.Small  -> 36.dp
        AppButtonSize.Medium -> 48.dp
        AppButtonSize.Large  -> 56.dp
    }
    val horizontalPadding: Dp = when (size) {
        AppButtonSize.Small  -> 12.dp
        AppButtonSize.Medium -> 20.dp
        AppButtonSize.Large  -> 28.dp
    }
    val fontSize = when (size) {
        AppButtonSize.Small  -> 13.sp
        AppButtonSize.Medium -> 15.sp
        AppButtonSize.Large  -> 17.sp
    }

    val containerColor = when (variant) {
        AppButtonVariant.Primary   -> if (enabled) AppColors.Primary else AppColors.Outline
        AppButtonVariant.Secondary -> AppColors.PrimaryLight
        AppButtonVariant.Outlined  -> Color.Transparent
        AppButtonVariant.Ghost     -> Color.Transparent
        AppButtonVariant.Danger    -> if (enabled) AppColors.Error else AppColors.Outline
    }
    val contentColor = when (variant) {
        AppButtonVariant.Primary   -> AppColors.TextOnPrimary
        AppButtonVariant.Secondary -> AppColors.Primary
        AppButtonVariant.Outlined  -> AppColors.Primary
        AppButtonVariant.Ghost     -> AppColors.Primary
        AppButtonVariant.Danger    -> AppColors.TextOnPrimary
    }
    val borderColor = when (variant) {
        AppButtonVariant.Outlined -> AppColors.Primary
        else -> Color.Transparent
    }

    val elevation: Dp by animateDpAsState(
        targetValue = when {
            !enabled                                          -> 0.dp
            isPressed && variant == AppButtonVariant.Primary -> 1.dp
            variant == AppButtonVariant.Primary              -> 4.dp
            else                                              -> 0.dp
        },
        animationSpec = tween(100), label = "buttonElevation"
    )

    Box(
        modifier = modifier
            .height(height)
            .shadow(elevation, AppShapes.Large)
            .clip(AppShapes.Large)
            .background(containerColor)
            .then(
                if (variant == AppButtonVariant.Outlined)
                    Modifier.border(1.5.dp, borderColor, AppShapes.Large)
                else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                enabled = enabled && !isLoading,
                onClick = onClick
            )
            .padding(horizontal = horizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = contentColor,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.S)
            ) {
                leadingIcon?.let {
                    Icon(it, contentDescription = null, tint = contentColor, modifier = Modifier.size(18.dp))
                }
                Text(
                    text = text,
                    color = if (enabled) contentColor else AppColors.TextDisabled,
                    fontSize = fontSize,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp
                )
                trailingIcon?.let {
                    Icon(it, contentDescription = null, tint = contentColor, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

// Icon-only button
@Composable
fun AppIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = AppColors.Primary,
    size: Dp = 40.dp,
    background: Color = AppColors.PrimaryLight,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(AppShapes.Medium)
            .background(background)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(size * 0.5f))
    }
}

// ─────────────────────────────────────────────
//  TEXT FIELDS
// ─────────────────────────────────────────────

enum class AppTextFieldVariant { Filled, Outlined }
enum class AppTextFieldState   { Default, Focused, Error, Success, Disabled }

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    helperText: String = "",
    errorText: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    isPassword: Boolean = false,
    isError: Boolean = false,
    isSuccess: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    variant: AppTextFieldVariant = AppTextFieldVariant.Outlined,
) {
    var isFocused by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = when {
            isError   -> AppColors.Error
            isSuccess -> AppColors.Success
            isFocused -> AppColors.OutlineFocus
            else      -> AppColors.Outline
        },
        animationSpec = tween(200), label = "borderColor"
    )
    val labelColor = when {
        isError   -> AppColors.Error
        isSuccess -> AppColors.Success
        isFocused -> AppColors.Primary
        else      -> AppColors.TextSecondary
    }
    val bgColor = when (variant) {
        AppTextFieldVariant.Filled   -> if (isFocused) AppColors.PrimaryLight else AppColors.SurfaceVariant
        AppTextFieldVariant.Outlined -> AppColors.Surface
    }

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                color = labelColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = AppSpacing.XS)
            )
        }

        val shape = AppShapes.Medium
        val borderModifier = when (variant) {
            AppTextFieldVariant.Outlined ->
                Modifier.border(
                    width = if (isFocused || isError || isSuccess) 2.dp else 1.dp,
                    color = borderColor,
                    shape = shape
                )
            AppTextFieldVariant.Filled -> Modifier
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = if (isPassword && !passwordVisible)
                PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textStyle = LocalTextStyle.current.copy(
                color = if (enabled) AppColors.TextPrimary else AppColors.TextDisabled,
                fontSize = 15.sp
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor, shape)
                        .then(borderModifier)
                        .padding(horizontal = AppSpacing.L, vertical = AppSpacing.M),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.S)
                ) {
                    leadingIcon?.let {
                        Icon(it, contentDescription = null,
                            tint = if (isFocused) AppColors.Primary else AppColors.TextSecondary,
                            modifier = Modifier.size(18.dp))
                    }
                    Box(Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(placeholder, color = AppColors.TextDisabled, fontSize = 15.sp)
                        }
                        innerTextField()
                    }
                    when {
                        isPassword -> {
                            Icon(
                                if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                                contentDescription = null,
                                tint = AppColors.TextSecondary,
                                modifier = Modifier.size(18.dp).clickable { passwordVisible = !passwordVisible }
                            )
                        }
                        isError -> Icon(Icons.Filled.Error, contentDescription = null,
                            tint = AppColors.Error, modifier = Modifier.size(18.dp))
                        isSuccess -> Icon(Icons.Filled.CheckCircle, contentDescription = null,
                            tint = AppColors.Success, modifier = Modifier.size(18.dp))
                        trailingIcon != null -> {
                            Icon(trailingIcon, contentDescription = null,
                                tint = AppColors.TextSecondary,
                                modifier = Modifier.size(18.dp).then(
                                    if (onTrailingIconClick != null) Modifier.clickable { onTrailingIconClick() }
                                    else Modifier
                                ))
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
        )

        val bottomText = when {
            isError && errorText.isNotEmpty() -> errorText
            helperText.isNotEmpty()           -> helperText
            else                              -> null
        }
        bottomText?.let {
            Text(
                text = it,
                color = if (isError) AppColors.Error else AppColors.TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = AppSpacing.XS, start = AppSpacing.XS)
            )
        }
    }
}

// Search Field
@Composable
fun AppSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Поиск...",
    onClear: (() -> Unit)? = null,
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        leadingIcon = Icons.Outlined.Search,
        trailingIcon = if (value.isNotEmpty()) Icons.Filled.Clear else null,
        onTrailingIconClick = onClear,
        variant = AppTextFieldVariant.Filled
    )
}

// ─────────────────────────────────────────────
//  CHECKBOX
// ─────────────────────────────────────────────

@Composable
fun AppCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    description: String = "",
    enabled: Boolean = true,
    isError: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val checkColor by animateColorAsState(
        targetValue = when {
            !enabled -> AppColors.Outline
            isError  -> AppColors.Error
            checked  -> AppColors.Primary
            else     -> AppColors.Outline
        },
        animationSpec = tween(150), label = "checkColor"
    )

    Row(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled
            ) { onCheckedChange(!checked) },
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.M)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 1.dp)
                .size(20.dp)
                .clip(AppShapes.Small)
                .background(if (checked) checkColor else Color.Transparent)
                .border(
                    width = if (checked) 0.dp else 2.dp,
                    color = checkColor,
                    shape = AppShapes.Small
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
            }
        }

        if (label.isNotEmpty() || description.isNotEmpty()) {
            Column {
                if (label.isNotEmpty()) {
                    Text(
                        text = label,
                        color = if (enabled) AppColors.TextPrimary else AppColors.TextDisabled,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        color = AppColors.TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────
//  RADIO BUTTON
// ─────────────────────────────────────────────

@Composable
fun AppRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .clickable(enabled = enabled, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.M)
    ) {
        val color by animateColorAsState(
            targetValue = if (selected) AppColors.Primary else AppColors.Outline,
            animationSpec = tween(150), label = "radioColor"
        )
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(2.dp, color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(AppColors.Primary)
                )
            }
        }
        if (label.isNotEmpty()) {
            Text(
                text = label,
                color = if (enabled) AppColors.TextPrimary else AppColors.TextDisabled,
                fontSize = 15.sp
            )
        }
    }
}

// ─────────────────────────────────────────────
//  TOGGLE SWITCH
// ─────────────────────────────────────────────

@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 20.dp else 2.dp,
        animationSpec = tween(200), label = "thumbOffset"
    )
    val trackColor by animateColorAsState(
        targetValue = if (checked) AppColors.Primary else AppColors.Outline,
        animationSpec = tween(200), label = "trackColor"
    )

    Row(
        modifier = modifier
            .clickable(enabled = enabled) { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.M)
    ) {
        Box(
            modifier = Modifier
                .width(44.dp).height(26.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(trackColor)
        ) {
            Box(
                modifier = Modifier
                    .offset(x = thumbOffset, y = 3.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .shadow(2.dp, CircleShape)
            )
        }
        if (label.isNotEmpty()) {
            Text(
                text = label,
                color = if (enabled) AppColors.TextPrimary else AppColors.TextDisabled,
                fontSize = 15.sp
            )
        }
    }
}

// ─────────────────────────────────────────────
//  CHIPS
// ─────────────────────────────────────────────

enum class AppChipVariant { Filter, Input, Suggestion, Status }

@Composable
fun AppChip(
    label: String,
    modifier: Modifier = Modifier,
    variant: AppChipVariant = AppChipVariant.Filter,
    selected: Boolean = false,
    leadingIcon: ImageVector? = null,
    onClose: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    val bgColor by animateColorAsState(
        targetValue = when {
            selected -> AppColors.PrimaryLight
            variant == AppChipVariant.Status -> AppColors.SurfaceVariant
            else -> AppColors.SurfaceVariant
        },
        animationSpec = tween(150), label = "chipBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) AppColors.Primary else Color.Transparent,
        animationSpec = tween(150), label = "chipBorder"
    )
    val textColor = if (selected) AppColors.Primary else AppColors.TextPrimary

    Row(
        modifier = modifier
            .clip(AppShapes.ExtraLarge)
            .background(bgColor)
            .border(1.5.dp, borderColor, AppShapes.ExtraLarge)
            .then(
                if (onClick != null) Modifier.clickable(enabled = enabled, onClick = onClick)
                else Modifier
            )
            .padding(horizontal = AppSpacing.L, vertical = AppSpacing.S),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.XS)
    ) {
        leadingIcon?.let {
            Icon(it, contentDescription = null, tint = textColor, modifier = Modifier.size(14.dp))
        }
        if (selected && variant == AppChipVariant.Filter) {
            Icon(Icons.Filled.Check, contentDescription = null,
                tint = AppColors.Primary, modifier = Modifier.size(14.dp))
        }
        Text(label, color = textColor, fontSize = 13.sp, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
        onClose?.let {
            Icon(Icons.Filled.Close, contentDescription = "Remove",
                tint = AppColors.TextSecondary,
                modifier = Modifier.size(14.dp).clickable { it() })
        }
    }
}

// Status chip with color
@Composable
fun AppStatusChip(
    label: String,
    modifier: Modifier = Modifier,
    color: Color = AppColors.Primary,
) {
    val bg = color.copy(alpha = 0.12f)
    Row(
        modifier = modifier
            .clip(AppShapes.ExtraLarge)
            .background(bg)
            .padding(horizontal = AppSpacing.M, vertical = AppSpacing.XS),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.XS)
    ) {
        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(color))
        Text(label, color = color, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

// ─────────────────────────────────────────────
//  SELECT / DROPDOWN
// ─────────────────────────────────────────────

@Composable
fun <T> AppSelect(
    value: T?,
    options: List<T>,
    onSelect: (T) -> Unit,
    label: String = "",
    placeholder: String = "Выберите...",
    modifier: Modifier = Modifier,
    displayText: (T) -> String = { it.toString() },
    enabled: Boolean = true,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                color = if (expanded) AppColors.Primary else AppColors.TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = AppSpacing.XS)
            )
        }

        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AppShapes.Medium)
                    .background(AppColors.Surface)
                    .border(
                        width = if (expanded) 2.dp else 1.dp,
                        color = if (expanded) AppColors.OutlineFocus else AppColors.Outline,
                        shape = AppShapes.Medium
                    )
                    .clickable(enabled = enabled) { expanded = !expanded }
                    .padding(horizontal = AppSpacing.L, vertical = AppSpacing.M),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (value != null) displayText(value) else placeholder,
                    color = if (value != null) AppColors.TextPrimary else AppColors.TextDisabled,
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f)
                )
                val rotation by animateDpAsState(
                    targetValue = 0.dp, label = "rotation"
                )
                Icon(
                    if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = if (expanded) AppColors.Primary else AppColors.TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(AppColors.Surface)
                    .shadow(AppElevation.Large, AppShapes.Medium)
            ) {
                options.forEach { option ->
                    val isSelected = option == value
                    DropdownMenuItem(
                        text = {
                            Text(
                                displayText(option),
                                color = if (isSelected) AppColors.Primary else AppColors.TextPrimary,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 15.sp
                            )
                        },
                        trailingIcon = {
                            if (isSelected) Icon(Icons.Filled.Check, null,
                                tint = AppColors.Primary, modifier = Modifier.size(16.dp))
                        },
                        onClick = {
                            onSelect(option)
                            expanded = false
                        },
                        modifier = Modifier.background(
                            if (isSelected) AppColors.PrimaryLight else Color.Transparent
                        )
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────
//  CARDS
// ─────────────────────────────────────────────

enum class AppCardVariant { Elevated, Outlined, Filled }

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    variant: AppCardVariant = AppCardVariant.Elevated,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val bgColor = when (variant) {
        AppCardVariant.Elevated -> AppColors.Surface
        AppCardVariant.Outlined -> AppColors.Surface
        AppCardVariant.Filled   -> AppColors.SurfaceVariant
    }
    val elevation = when (variant) {
        AppCardVariant.Elevated -> AppElevation.Medium
        else -> AppElevation.None
    }

    val cardModifier = modifier
        .shadow(elevation, AppShapes.Large)
        .clip(AppShapes.Large)
        .background(bgColor)
        .then(
            if (variant == AppCardVariant.Outlined)
                Modifier.border(1.dp, AppColors.Outline, AppShapes.Large)
            else Modifier
        )
        .then(
            if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        )

    Column(modifier = cardModifier, content = content)
}

// Media card with image header
@Composable
fun AppMediaCard(
    title: String,
    subtitle: String = "",
    badge: String = "",
    badgeColor: Color = AppColors.Primary,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    imageContent: @Composable BoxScope.() -> Unit = {},
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    AppCard(modifier = modifier, onClick = onClick) {
        Box(
            modifier = Modifier.fillMaxWidth().height(160.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(AppColors.SurfaceVariant)
        ) {
            imageContent()
            if (badge.isNotEmpty()) {
                AppStatusChip(
                    label = badge,
                    color = badgeColor,
                    modifier = Modifier.align(Alignment.TopEnd).padding(AppSpacing.M)
                )
            }
        }
        Column(modifier = Modifier.padding(AppSpacing.L)) {
            Text(title, color = AppColors.TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            if (subtitle.isNotEmpty()) {
                Spacer(Modifier.height(AppSpacing.XS))
                Text(subtitle, color = AppColors.TextSecondary, fontSize = 13.sp)
            }
            actions?.let {
                Spacer(Modifier.height(AppSpacing.M))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    content = it
                )
            }
        }
    }
}

// Stats card
@Composable
fun AppStatsCard(
    title: String,
    value: String,
    change: String = "",
    isPositive: Boolean = true,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier,
) {
    AppCard(modifier = modifier) {
        Row(
            modifier = Modifier.padding(AppSpacing.L).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(title, color = AppColors.TextSecondary, fontSize = 13.sp)
                Spacer(Modifier.height(AppSpacing.XS))
                Text(value, color = AppColors.TextPrimary, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                if (change.isNotEmpty()) {
                    Spacer(Modifier.height(AppSpacing.XS))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Icon(
                            if (isPositive) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown,
                            contentDescription = null,
                            tint = if (isPositive) AppColors.Success else AppColors.Error,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(change,
                            color = if (isPositive) AppColors.Success else AppColors.Error,
                            fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
            icon?.let {
                Box(
                    modifier = Modifier.size(44.dp).clip(AppShapes.Medium).background(AppColors.PrimaryLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(it, contentDescription = null, tint = AppColors.Primary, modifier = Modifier.size(22.dp))
                }
            }
        }
    }
}

// ─────────────────────────────────────────────
//  BADGE
// ─────────────────────────────────────────────

@Composable
fun AppBadge(
    count: Int,
    modifier: Modifier = Modifier,
    color: Color = AppColors.Error,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        content()
        if (count > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
                    .defaultMinSize(minWidth = 18.dp, minHeight = 18.dp)
                    .clip(CircleShape)
                    .background(color)
                    .padding(horizontal = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (count > 99) "99+" else count.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ─────────────────────────────────────────────
//  DIVIDER
// ─────────────────────────────────────────────

@Composable
fun AppDivider(
    modifier: Modifier = Modifier,
    label: String = "",
    color: Color = AppColors.Outline,
) {
    if (label.isEmpty()) {
        HorizontalDivider(modifier = modifier, color = color, thickness = 1.dp)
    } else {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = color)
            Text(
                text = label,
                color = AppColors.TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = AppSpacing.M)
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = color)
        }
    }
}

// ─────────────────────────────────────────────
//  SNACKBAR / TOAST MESSAGES
// ─────────────────────────────────────────────

enum class AppAlertType { Info, Success, Warning, Error }

@Composable
fun AppAlert(
    message: String,
    type: AppAlertType = AppAlertType.Info,
    title: String = "",
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null,
) {
    val (bgColor, iconColor, icon) = when (type) {
        AppAlertType.Info    -> Triple(AppColors.Info.copy(alpha = 0.1f),    AppColors.Info,    Icons.Outlined.Info)
        AppAlertType.Success -> Triple(AppColors.Success.copy(alpha = 0.1f), AppColors.Success, Icons.Filled.CheckCircle)
        AppAlertType.Warning -> Triple(AppColors.Warning.copy(alpha = 0.1f), AppColors.Warning, Icons.Outlined.Warning)
        AppAlertType.Error   -> Triple(AppColors.Error.copy(alpha = 0.1f),   AppColors.Error,   Icons.Filled.Error)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppShapes.Medium)
            .background(bgColor)
            .border(1.dp, iconColor.copy(alpha = 0.3f), AppShapes.Medium)
            .padding(AppSpacing.L),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.M)
    ) {
        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp).padding(top = 1.dp))
        Column(modifier = Modifier.weight(1f)) {
            if (title.isNotEmpty()) {
                Text(title, color = iconColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(2.dp))
            }
            Text(message, color = AppColors.TextPrimary, fontSize = 14.sp)
        }
        onDismiss?.let {
            Icon(Icons.Filled.Close, contentDescription = "Закрыть",
                tint = AppColors.TextSecondary,
                modifier = Modifier.size(18.dp).clickable { it() })
        }
    }
}

// ─────────────────────────────────────────────
//  AVATAR
// ─────────────────────────────────────────────

@Composable
fun AppAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    backgroundColor: Color = AppColors.PrimaryLight,
    textColor: Color = AppColors.Primary,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials.take(2).uppercase(),
            color = textColor,
            fontSize = (size.value * 0.35).sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ─────────────────────────────────────────────
//  PROGRESS INDICATORS
// ─────────────────────────────────────────────

@Composable
fun AppProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = AppColors.Primary,
    trackColor: Color = AppColors.PrimaryLight,
    height: Dp = 8.dp,
    showLabel: Boolean = false,
) {
    Column {
        if (showLabel) {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = AppSpacing.XS),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Прогресс", color = AppColors.TextSecondary, fontSize = 12.sp)
                Text("${(progress * 100).toInt()}%", color = AppColors.Primary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(height / 2))
                .background(trackColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(height / 2))
                    .background(color)
            )
        }
    }
}


// ─────────────────────────────────────────────
//  FOCUS EXTENSION (for text fields)
// ─────────────────────────────────────────────



// ─────────────────────────────────────────────
//  PREVIEW
// ─────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FC)
@Composable
fun UIKitPreview() {
    var text by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var checked1 by remember { mutableStateOf(true) }
    var checked2 by remember { mutableStateOf(false) }
    var radioSelected by remember { mutableStateOf(0) }
    var switchOn by remember { mutableStateOf(true) }
    var chipSelected by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }

    val options = listOf("Разработка", "Дизайн", "Маркетинг", "Аналитика")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppSpacing.XL)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.XL)
    ) {
        // BUTTONS
        Text("Кнопки", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.M)) {
            AppButton("Primary", {}, variant = AppButtonVariant.Primary, size = AppButtonSize.Small)
            AppButton("Secondary", {}, variant = AppButtonVariant.Secondary, size = AppButtonSize.Small)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.M)) {
            AppButton("Outlined", {}, variant = AppButtonVariant.Outlined, size = AppButtonSize.Small)
            AppButton("Ghost", {}, variant = AppButtonVariant.Ghost, size = AppButtonSize.Small)
            AppButton("Danger", {}, variant = AppButtonVariant.Danger, size = AppButtonSize.Small)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.M)) {
            AppButton("С иконкой", {}, leadingIcon = Icons.Filled.Add)
            AppButton("Загрузка", {}, isLoading = true)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.M)) {
            AppIconButton(Icons.Filled.Favorite, {})
            AppIconButton(Icons.Filled.Share, {}, background = AppColors.SurfaceVariant, tint = AppColors.TextPrimary)
        }

        AppDivider()

        // TEXT FIELDS
        Text("Поля ввода", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
        AppTextField(
            value = text, onValueChange = { text = it },
            label = "Email", placeholder = "user@example.com",
            leadingIcon = Icons.Outlined.Email,
            helperText = "Мы никогда не спамим"
        )
        AppTextField(
            value = password, onValueChange = { password = it },
            label = "Пароль", placeholder = "••••••••",
            isPassword = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        AppTextField(
            value = "Ошибка", onValueChange = {},
            label = "Поле с ошибкой",
            isError = true, errorText = "Введите корректные данные"
        )
        AppTextField(
            value = "Всё ок", onValueChange = {},
            label = "Успешное поле",
            isSuccess = true
        )
        AppSearchField(value = "", onValueChange = {})

        AppDivider()

        // CHECKBOXES & RADIO
        Text("Чекбоксы и радио", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
        AppCheckbox(checked = checked1, onCheckedChange = { checked1 = it },
            label = "Получать уведомления", description = "Push и email")
        AppCheckbox(checked = checked2, onCheckedChange = { checked2 = it },
            label = "Отключено", enabled = false)
        AppCheckbox(checked = true, onCheckedChange = {},
            label = "С ошибкой", isError = true)

        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.M)) {
            listOf("Вариант А", "Вариант Б", "Вариант В").forEachIndexed { i, label ->
                AppRadioButton(selected = radioSelected == i, onClick = { radioSelected = i }, label = label)
            }
        }

        AppDivider()

        // SWITCHES
        Text("Переключатели", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
        AppSwitch(checked = switchOn, onCheckedChange = { switchOn = it }, label = "Тёмная тема")
        AppSwitch(checked = false, onCheckedChange = {}, label = "Отключено", enabled = false)

        AppDivider()

        // CHIPS
        Text("Чипы", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.S)) {
            listOf("UI", "UX", "Figma", "Compose").forEachIndexed { i, label ->
                AppChip(
                    label = label,
                    variant = AppChipVariant.Filter,
                    selected = chipSelected == i,
                    onClick = { chipSelected = i }
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.S)) {
            AppChip("Kotlin", variant = AppChipVariant.Input, leadingIcon = Icons.Filled.Code,
                onClose = {})
            AppChip("Android", variant = AppChipVariant.Input, onClose = {})
        }
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.S)) {
            AppStatusChip("Активен", color = AppColors.Success)
            AppStatusChip("В ожидании", color = AppColors.Warning)
            AppStatusChip("Ошибка", color = AppColors.Error)
            AppStatusChip("Информация", color = AppColors.Info)
        }

        AppDivider()

        // SELECT
        Text("Селект", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
        AppSelect(
            value = selectedOption,
            options = options,
            onSelect = { selectedOption = it },
            label = "Отдел",
            placeholder = "Выберите отдел..."
        )

        AppDivider()

        // CARDS
        Text("Карточки", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
        AppStatsCard(
            title = "Пользователи",
            value = "12,430",
            change = "+8.2% за месяц",
            isPositive = true,
            icon = Icons.Filled.People
        )
        AppMediaCard(
            title = "Jetpack Compose UI Kit",
            subtitle = "Полный набор компонентов для ваших приложений",
            badge = "Новое",
            badgeColor = AppColors.Success,
            actions = {
                AppButton("Открыть", {}, size = AppButtonSize.Small)
            }
        )
        AppCard(variant = AppCardVariant.Outlined) {
            Row(
                modifier = Modifier.padding(AppSpacing.L).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.M)
            ) {
                AppAvatar("АП", size = 44.dp)
                Column(Modifier.weight(1f)) {
                    Text("Алексей Петров", fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary)
                    Text("Senior Android Developer", color = AppColors.TextSecondary, fontSize = 13.sp)
                }
                AppBadge(count = 3) {
                    AppIconButton(Icons.Outlined.Notifications, {})
                }
            }
        }

        AppDivider()

        // ALERTS
        Text("Алерты", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
        AppAlert("Ваши данные успешно сохранены.", type = AppAlertType.Success, title = "Готово!")
        AppAlert("Проверьте введённые данные.", type = AppAlertType.Warning)
        AppAlert("Что-то пошло не так. Попробуйте снова.", type = AppAlertType.Error, onDismiss = {})
        AppAlert("Доступна новая версия приложения.", type = AppAlertType.Info)

        AppDivider()

        // PROGRESS
        Text("Прогресс", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
        AppProgressBar(progress = 0.72f, showLabel = true)
        AppProgressBar(progress = 0.4f, color = AppColors.Success, trackColor = AppColors.Success.copy(alpha = 0.12f), height = 6.dp)
        AppProgressBar(progress = 0.9f, color = AppColors.Warning, trackColor = AppColors.Warning.copy(alpha = 0.12f), height = 12.dp)

        Spacer(Modifier.height(AppSpacing.XXL))
    }
}