package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "universities",
    indices = [
        Index(value = ["shortName"], unique = true),
        Index(value = ["name"]),
        Index(value = ["isPopular"])
    ]
)
data class UniversityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val shortName: String,
    val location: String,
    val type: String, // "Private" or "Public"
    val studentCount: Int = 0,
    val postsCount: Int = 0,
    val isPopular: Boolean = false,
    val primaryColorHex: String = "#4F46E5",
    val logoEmoji: String = "🎓"
)
