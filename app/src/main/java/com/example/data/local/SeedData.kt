package com.example.data.local

import com.example.data.local.entity.BadgeEntity
import com.example.data.local.entity.DepartmentEntity
import com.example.data.local.entity.UniversityEntity

object SeedData {
    val universities = listOf(
        UniversityEntity(
            id = 1,
            name = "Shanto-Mariam University of Creative Technology",
            shortName = "SMUCT",
            location = "Uttara, Dhaka",
            type = "Private",
            studentCount = 1420,
            postsCount = 380,
            isPopular = true,
            primaryColorHex = "#4F46E5",
            logoEmoji = "🎨"
        ),
        UniversityEntity(
            id = 2,
            name = "University of Dhaka",
            shortName = "DU",
            location = "Nilkhet, Dhaka",
            type = "Public",
            studentCount = 3850,
            postsCount = 920,
            isPopular = true,
            primaryColorHex = "#DC2626",
            logoEmoji = "🏛️"
        ),
        UniversityEntity(
            id = 3,
            name = "Bangladesh University of Engineering and Technology",
            shortName = "BUET",
            location = "Palashi, Dhaka",
            type = "Public",
            studentCount = 2900,
            postsCount = 740,
            isPopular = true,
            primaryColorHex = "#059669",
            logoEmoji = "⚙️"
        ),
        UniversityEntity(
            id = 4,
            name = "North South University",
            shortName = "NSU",
            location = "Bashundhara R/A, Dhaka",
            type = "Private",
            studentCount = 3100,
            postsCount = 850,
            isPopular = true,
            primaryColorHex = "#2563EB",
            logoEmoji = "🦅"
        ),
        UniversityEntity(
            id = 5,
            name = "BRAC University",
            shortName = "BRACU",
            location = "Merul Badda, Dhaka",
            type = "Private",
            studentCount = 2750,
            postsCount = 670,
            isPopular = true,
            primaryColorHex = "#7C3AED",
            logoEmoji = "🌐"
        ),
        UniversityEntity(
            id = 6,
            name = "American International University-Bangladesh",
            shortName = "AIUB",
            location = "Kuratoli, Dhaka",
            type = "Private",
            studentCount = 2100,
            postsCount = 510,
            isPopular = true,
            primaryColorHex = "#0284C7",
            logoEmoji = "🚀"
        ),
        UniversityEntity(
            id = 7,
            name = "Islamic University of Technology",
            shortName = "IUT",
            location = "Board Bazar, Gazipur",
            type = "International",
            studentCount = 1850,
            postsCount = 490,
            isPopular = true,
            primaryColorHex = "#0D9488",
            logoEmoji = "🔬"
        ),
        UniversityEntity(
            id = 8,
            name = "Shahjalal University of Science and Technology",
            shortName = "SUST",
            location = "Kumargaon, Sylhet",
            type = "Public",
            studentCount = 2400,
            postsCount = 610,
            isPopular = true,
            primaryColorHex = "#16A34A",
            logoEmoji = "🌲"
        ),
        UniversityEntity(
            id = 9,
            name = "Independent University, Bangladesh",
            shortName = "IUB",
            location = "Bashundhara, Dhaka",
            type = "Private",
            studentCount = 1950,
            postsCount = 430,
            isPopular = false,
            primaryColorHex = "#D97706",
            logoEmoji = "📚"
        ),
        UniversityEntity(
            id = 10,
            name = "Ahsanullah University of Science and Technology",
            shortName = "AUST",
            location = "Tejgaon, Dhaka",
            type = "Private",
            studentCount = 1700,
            postsCount = 390,
            isPopular = false,
            primaryColorHex = "#9333EA",
            logoEmoji = "📐"
        ),
        UniversityEntity(
            id = 11,
            name = "Jahangirnagar University",
            shortName = "JU",
            location = "Savar, Dhaka",
            type = "Public",
            studentCount = 2200,
            postsCount = 540,
            isPopular = false,
            primaryColorHex = "#EA580C",
            logoEmoji = "🦋"
        ),
        UniversityEntity(
            id = 12,
            name = "United International University",
            shortName = "UIU",
            location = "Madani Avenue, Dhaka",
            type = "Private",
            studentCount = 1600,
            postsCount = 320,
            isPopular = false,
            primaryColorHex = "#E11D48",
            logoEmoji = "💡"
        ),
        UniversityEntity(
            id = 13,
            name = "Daffodil International University",
            shortName = "DIU",
            location = "Daffodil Smart City, Birulia",
            type = "Private",
            studentCount = 2800,
            postsCount = 620,
            isPopular = false,
            primaryColorHex = "#059669",
            logoEmoji = "🌿"
        ),
        UniversityEntity(
            id = 14,
            name = "East West University",
            shortName = "EWU",
            location = "Aftabnagar, Dhaka",
            type = "Private",
            studentCount = 1900,
            postsCount = 410,
            isPopular = false,
            primaryColorHex = "#4338CA",
            logoEmoji = "☀️"
        )
    )

    val departments = listOf(
        DepartmentEntity(1, "CSE", "Computer Science & Engineering", "💻"),
        DepartmentEntity(2, "BBA", "Bachelor of Business Administration", "📊"),
        DepartmentEntity(3, "EEE", "Electrical & Electronic Engineering", "⚡"),
        DepartmentEntity(4, "FDT", "Fashion Design & Technology", "👗"),
        DepartmentEntity(5, "GDM", "Graphic Design & Multimedia", "🎨"),
        DepartmentEntity(6, "ARCH", "Architecture", "📐"),
        DepartmentEntity(7, "LAW", "Law & Justice", "⚖️"),
        DepartmentEntity(8, "ENG", "English Literature & Linguistics", "📖"),
        DepartmentEntity(9, "PHARM", "Pharmacy", "💊"),
        DepartmentEntity(10, "CIVIL", "Civil Engineering", "🏗️"),
        DepartmentEntity(11, "JRN", "Journalism & Media Studies", "🎙️"),
        DepartmentEntity(12, "ECO", "Economics", "📈")
    )

    val badges = listOf(
        BadgeEntity(
            id = 1,
            badgeKey = "early_member",
            title = "Early Member",
            description = "Joined UniLoop during initial campus launch",
            iconEmoji = "🏅",
            requiredReferrals = 3
        ),
        BadgeEntity(
            id = 2,
            badgeKey = "theme_unlocked",
            title = "Campus Stylist",
            description = "Unlocked premium profile gradient theme",
            iconEmoji = "🎨",
            requiredReferrals = 10
        ),
        BadgeEntity(
            id = 3,
            badgeKey = "profile_boost",
            title = "Campus Rocket",
            description = "Priority discovery in campus feed & search",
            iconEmoji = "🚀",
            requiredReferrals = 25
        ),
        BadgeEntity(
            id = 4,
            badgeKey = "campus_ambassador",
            title = "Campus Ambassador",
            description = "Official university representative badge",
            iconEmoji = "👑",
            requiredReferrals = 50
        ),
        BadgeEntity(
            id = 5,
            badgeKey = "founding_member",
            title = "UniLoop Founding Member",
            description = "Hall of fame member with permanent golden verification",
            iconEmoji = "🏆",
            requiredReferrals = 100
        )
    )

    val batches = (2019..2028).map { it.toString() }.reversed()
}
