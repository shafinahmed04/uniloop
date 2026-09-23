package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.UniIndigoContainer
import com.example.ui.theme.UniIndigoPrimary
import com.example.ui.theme.UniTealContainer
import com.example.ui.theme.UniTealSecondary

@Composable
fun CampusBadge(
    department: String,
    universityShortName: String,
    batch: String,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false
) {
    val displayText = buildString {
        append("🎓 ")
        if (department.isNotBlank()) {
            append(department)
            append(" • ")
        }
        append(universityShortName.ifBlank { "UniLoop" })
        if (batch.isNotBlank()) {
            append(" • Batch ")
            append(batch)
        }
    }

    if (isHighlighted) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            UniIndigoPrimary.copy(alpha = 0.15f),
                            UniTealSecondary.copy(alpha = 0.15f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        listOf(UniIndigoPrimary, UniTealSecondary)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayText,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    } else {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
