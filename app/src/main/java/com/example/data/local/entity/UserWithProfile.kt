package com.example.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithProfile(
    @Embedded
    val user: UserEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val profile: ProfileEntity?
) {
    val campusDisplayName: String
        get() = buildString {
            if (profile != null) {
                append("🎓 ")
                if (profile.department.isNotBlank()) {
                    append(profile.department)
                    append(" • ")
                }
                append(profile.universityShortName.ifBlank { "UniLoop" })
                if (profile.batch.isNotBlank()) {
                    append(" • Batch ")
                    append(profile.batch)
                }
            } else {
                append("🎓 UniLoop Student")
            }
        }
}
