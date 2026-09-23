package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.UniAmberTertiary
import com.example.ui.theme.UniIndigoLight
import com.example.ui.theme.UniIndigoPrimary
import com.example.ui.theme.UniTealLight
import com.example.ui.theme.UniTealSecondary

@Composable
fun UniLoopLogo(
    modifier: Modifier = Modifier,
    size: LogoSize = LogoSize.MEDIUM,
    showTagline: Boolean = true
) {
    val iconSize = when (size) {
        LogoSize.SMALL -> 32.dp
        LogoSize.MEDIUM -> 48.dp
        LogoSize.LARGE -> 72.dp
    }

    val titleFontSize = when (size) {
        LogoSize.SMALL -> 18.sp
        LogoSize.MEDIUM -> 26.sp
        LogoSize.LARGE -> 36.sp
    }

    val taglineFontSize = when (size) {
        LogoSize.SMALL -> 10.sp
        LogoSize.MEDIUM -> 12.sp
        LogoSize.LARGE -> 14.sp
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stylized Loop Icon Badge
            Box(
                modifier = Modifier
                    .size(iconSize)
                    .clip(RoundedCornerShape(iconSize / 3))
                    .background(
                        Brush.linearGradient(
                            listOf(UniIndigoPrimary, UniTealSecondary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "∞",
                    fontSize = (titleFontSize.value * 1.1).sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "Uni",
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Loop",
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.secondary,
                    letterSpacing = (-0.5).sp
                )
                Box(
                    modifier = Modifier
                        .padding(bottom = 6.dp, start = 2.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(UniAmberTertiary)
                )
            }
        }

        if (showTagline) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Your Campus. Your People.",
                fontSize = taglineFontSize,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.5.sp
            )
        }
    }
}

enum class LogoSize {
    SMALL, MEDIUM, LARGE
}
