package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "profiles",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"], unique = true),
        Index(value = ["referralCode"], unique = true)
    ]
)
data class ProfileEntity(
    @PrimaryKey
    val userId: Long,
    val universityId: Long = 0,
    val universityName: String = "",
    val universityShortName: String = "",
    val department: String = "",
    val batch: String = "",
    val bio: String = "",
    val avatarUrl: String = "",
    val coverUrl: String = "",
    val referralCode: String = "",
    val referredByCode: String? = null,
    val isProfileComplete: Boolean = false,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val postsCount: Int = 0,
    val referralCount: Int = 0,
    val badges: String = "early_member" // Comma-separated badge identifiers
)
