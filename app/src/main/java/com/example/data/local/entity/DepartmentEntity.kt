package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "departments")
data class DepartmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val code: String, // e.g. "CSE", "BBA", "FDT", "EEE"
    val fullName: String,
    val iconEmoji: String = "💻"
)
