package app.mak.atmosense.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
  displayLarge = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    lineHeight = 40.sp,
    letterSpacing = 0.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
  ),
  bodyLarge = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.5.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
  ),
  labelSmall = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 10.sp,
    lineHeight = 14.sp,
    letterSpacing = 1.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false)
  )
)
