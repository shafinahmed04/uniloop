package com.example.data.models

import com.example.data.local.entity.ProfileEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UserWithProfile

/**
 * Domain model representing a UniLoop user.
 *
 * Designed around UniLoop's campus social networking concept:
 * includes student identity, university affiliation (University, Department, Batch),
 * social metrics, and campus badge formatting.
 */
data class User(
    val id: Long = 0,
    val name: String,
    val username: String,
    val email: String,
    val universityAffiliation: UniversityAffiliation = UniversityAffiliation(),
    val bio: String = "",
    val avatarUrl: String = "",
    val coverUrl: String = "",
    val role: String = "student", // "student", "ambassador", "admin", "moderator"
    val isVerified: Boolean = false,
    val isActive: Boolean = true,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val postsCount: Int = 0,
    val referralCount: Int = 0,
    val referralCode: String = "",
    val badges: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) {
    /**
     * Formats the student's campus identity badge according to UniLoop standards:
     * e.g., "🎓 CSE • SMUCT • Batch 2024"
     */
    val campusBadge: String
        get() = universityAffiliation.campusBadge

    companion object {
        fun fromEntity(user: UserEntity, profile: ProfileEntity?): User {
            val affiliation = UniversityAffiliation(
                universityId = profile?.universityId ?: 0,
                universityName = profile?.universityName.orEmpty(),
                universityShortName = profile?.universityShortName.orEmpty(),
                department = profile?.department.orEmpty(),
                batch = profile?.batch.orEmpty()
            )

            val badgeList = profile?.badges
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?: emptyList()

            return User(
                id = user.id,
                name = user.fullName,
                username = user.username,
                email = user.email,
                universityAffiliation = affiliation,
                bio = profile?.bio.orEmpty(),
                avatarUrl = profile?.avatarUrl.orEmpty(),
                coverUrl = profile?.coverUrl.orEmpty(),
                role = user.role,
                isVerified = user.isVerified,
                isActive = user.isActive,
                followersCount = profile?.followersCount ?: 0,
                followingCount = profile?.followingCount ?: 0,
                postsCount = profile?.postsCount ?: 0,
                referralCount = profile?.referralCount ?: 0,
                referralCode = profile?.referralCode.orEmpty(),
                badges = badgeList,
                createdAt = user.createdAt
            )
        }
    }
}

/**
 * University affiliation representing the student's academic background in Bangladesh.
 * Example:
 * University: Shanto-Mariam University of Creative Technology (SMUCT)
 * Department: CSE
 * Batch: 2024
 */
data class UniversityAffiliation(
    val universityId: Long = 0,
    val universityName: String = "",
    val universityShortName: String = "",
    val department: String = "",
    val batch: String = ""
) {
    /**
     * Renders badge as defined by UniLoop specification:
     * "🎓 CSE • SMUCT • Batch 2024"
     */
    val campusBadge: String
        get() = buildString {
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
}

/**
 * Extension mapper to convert a Room relational [UserWithProfile] into a domain [User].
 */
fun UserWithProfile.toUser(): User = User.fromEntity(user = this.user, profile = this.profile)
