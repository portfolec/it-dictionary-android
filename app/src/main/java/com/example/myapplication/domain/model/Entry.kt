package com.example.myapplication.domain.model

data class Entry(
    val id: String,
    val title: String,
    val shortDescription: String,
    val fullDescription: String,
    val codeExample: String,
    val category: String,
    val tags: List<String>,
    val relatedTerms: List<String>,
    val views: Int,
    val updatedAt: String,
    val isPublished: Boolean = true,
    val isBookmarked: Boolean = false
)

data class Category(
    val name: String,
    val colorHex: String,
    val iconName: String,
    val entryCount: Int = 0
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val viewedCount: Int = 0,
    val bookmarksCount: Int = 0,
    val daysInApp: Int = 0
)

enum class UserRole { USER, ADMIN }

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean,
    val category: String
)

data class AdminStats(
    val totalEntries: Int,
    val totalCategories: Int,
    val totalUsers: Int,
    val todayViews: Int
)
