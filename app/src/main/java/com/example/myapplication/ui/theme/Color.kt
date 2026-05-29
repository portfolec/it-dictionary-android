package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Color

// IT Справочник Design System

val NavyDark = Color(0xFF0A1628)
val ElectricBlue = Color(0xFF2D7DD2)
val ElectricBlueLight = Color(0xFF1e5a9a)
val White = Color(0xFFFFFFFF)
val GrayBackground = Color(0xFFF5F7FA)
val TextPrimary = Color(0xFF1A1A2E)
val TextSecondary = Color(0xFF6B7280)
val Success = Color(0xFF10B981)
val Error = Color(0xFFEF4444)
val SurfaceCard = Color(0xFFFFFFFF)
val DividerColor = Color(0xFFE5E7EB)
val DarkCodeBg = Color(0xFF1E293B)
val LoadingBarBg = Color(0xFF1A2942)

// Category colors
val CategoryLanguages = Color(0xFF6366F1)
val CategoryFrameworks = Color(0xFFF59E0B)
val CategoryDatabases = Color(0xFF10B981)
val CategoryDevOps = Color(0xFFEF4444)
val CategoryAlgorithms = Color(0xFF8B5CF6)
val CategoryPatterns = Color(0xFF06B6D4)
val CategoryNetworks = Color(0xFFF97316)
val CategorySecurity = Color(0xFFEC4899)

fun getCategoryColor(category: String): Color = when (category) {
    "Языки" -> CategoryLanguages
    "Фреймворки" -> CategoryFrameworks
    "Базы данных" -> CategoryDatabases
    "DevOps" -> CategoryDevOps
    "Алгоритмы" -> CategoryAlgorithms
    "Паттерны" -> CategoryPatterns
    "Сети" -> CategoryNetworks
    "ИБ" -> CategorySecurity
    else -> ElectricBlue
}
