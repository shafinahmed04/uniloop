package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.UniIndigoLight
import com.example.ui.theme.UniIndigoPrimary
import com.example.ui.theme.UniTealSecondary

data class CampusAvatar(
    val id: String,
    val emoji: String,
    val label: String,
    val backgroundColors: List<Color>
)

val defaultCampusAvatars = listOf(
    CampusAvatar("avatar_1", "👨‍💻", "Coder", listOf(Color(0xFF4F46E5), Color(0xFF06B6D4))),
    CampusAvatar("avatar_2", "👩‍🎨", "Artist", listOf(Color(0xFFEC4899), Color(0xFF8B5CF6))),
    CampusAvatar("avatar_3", "🧑‍🔬", "Techie", listOf(Color(0xFF10B981), Color(0xFF3B82F6))),
    CampusAvatar("avatar_4", "👩‍💼", "Leader", listOf(Color(0xFFF59E0B), Color(0xFFEF4444))),
    CampusAvatar("avatar_5", "🎧", "Vibes", listOf(Color(0xFF6366F1), Color(0xFFA855F7))),
    CampusAvatar("avatar_6", "⚽", "Athlete", listOf(Color(0xFF059669), Color(0xFF14B8A6))),
    CampusAvatar("avatar_7", "📸", "Creator", listOf(Color(0xFFD946EF), Color(0xFFF43F5E))),
    CampusAvatar("avatar_8", "📚", "Scholar", listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8)))
)

@Composable
fun UserAvatarView(
    avatarId: String,
    name: String,
    size: Dp = 44.dp,
    modifier: Modifier = Modifier
) {
    val avatar = defaultCampusAvatars.find { it.id == avatarId }
    val initial = name.trim().firstOrNull()?.uppercase() ?: "U"

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                if (avatar != null) {
                    Brush.linearGradient(avatar.backgroundColors)
                } else {
                    Brush.linearGradient(listOf(UniIndigoPrimary, UniTealSecondary))
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (avatar != null) {
            Text(
                text = avatar.emoji,
                fontSize = (size.value * 0.52).sp
            )
        } else {
            Text(
                text = initial,
                fontSize = (size.value * 0.45).sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun AvatarPickerRow(
    selectedAvatarId: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Pick Your Campus Persona",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(defaultCampusAvatars) { item ->
                val isSelected = item.id == selectedAvatarId
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(item.backgroundColors))
                        .then(
                            if (isSelected) {
                                Modifier.border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            } else Modifier
                        )
                        .clickable { onSelect(item.id) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.emoji,
                        fontSize = 28.sp
                    )
                }
            }
        }
    }
}
