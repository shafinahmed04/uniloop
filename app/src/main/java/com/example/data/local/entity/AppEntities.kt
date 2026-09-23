package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val authorId: Long,
    val authorName: String,
    val authorUsername: String,
    val authorAvatarUrl: String,
    val authorUniversity: String,
    val authorDepartment: String,
    val authorBatch: String,
    val universityId: Long = 0,
    val content: String,
    val mediaUrls: String = "", // Comma-separated or image identifier
    val postType: String = "status", // "status", "photo", "meme", "video", "moment"
    val audience: String = "public", // "public", "campus", "followers"
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val savesCount: Int = 0,
    val hashtags: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val postId: Long,
    val authorId: Long,
    val authorName: String,
    val authorUsername: String,
    val authorAvatarUrl: String,
    val authorUniversity: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val likesCount: Int = 0
)

@Entity(tableName = "post_likes")
data class PostLikeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val postId: Long,
    val userId: Long,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "follows")
data class FollowEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val followerId: Long,
    val followingId: Long,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val actorId: Long,
    val actorName: String,
    val actorAvatar: String,
    val type: String, // "like", "comment", "follow", "referral", "badge", "campus"
    val message: String,
    val targetPostId: Long? = null,
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "referrals")
data class ReferralEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val referrerUserId: Long,
    val referredUserId: Long,
    val referredUserName: String,
    val referralCode: String,
    val status: String = "verified", // "pending", "verified"
    val rewardGranted: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = System.currentTimeMillis()
)

@Entity(tableName = "badges")
data class BadgeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val badgeKey: String, // "early_member", "theme_unlocked", "profile_boost", "campus_ambassador", "founding_member"
    val title: String,
    val description: String,
    val iconEmoji: String,
    val requiredReferrals: Int
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reporterUserId: Long,
    val targetType: String, // "post", "user", "comment"
    val targetId: Long,
    val reason: String, // "spam", "harassment", "hate", "inappropriate", "other"
    val status: String = "pending", // "pending", "reviewed", "dismissed"
    val createdAt: Long = System.currentTimeMillis()
)
