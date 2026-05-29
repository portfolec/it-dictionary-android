package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.domain.model.Entry

@Entity(tableName = "entries")
data class EntryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val shortDescription: String,
    val fullDescription: String,
    val codeExample: String,
    val category: String,
    val tags: String,
    val relatedTerms: String,
    val views: Int,
    val updatedAt: String,
    val isPublished: Boolean,
    val isBookmarked: Boolean
) {
    fun toDomain() = Entry(
        id = id,
        title = title,
        shortDescription = shortDescription,
        fullDescription = fullDescription,
        codeExample = codeExample,
        category = category,
        tags = if (tags.isBlank()) emptyList() else tags.split(","),
        relatedTerms = if (relatedTerms.isBlank()) emptyList() else relatedTerms.split(","),
        views = views,
        updatedAt = updatedAt,
        isPublished = isPublished,
        isBookmarked = isBookmarked
    )
}

fun Entry.toEntity() = EntryEntity(
    id = id,
    title = title,
    shortDescription = shortDescription,
    fullDescription = fullDescription,
    codeExample = codeExample,
    category = category,
    tags = tags.joinToString(","),
    relatedTerms = relatedTerms.joinToString(","),
    views = views,
    updatedAt = updatedAt,
    isPublished = isPublished,
    isBookmarked = isBookmarked
)
