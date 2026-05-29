package com.example.myapplication.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.example.myapplication.domain.model.Entry

data class EntryDto(
    @SerializedName("id")               val id: String,
    @SerializedName("title")            val title: String,
    @SerializedName("shortDescription") val shortDescription: String = "",
    @SerializedName("fullDescription")  val fullDescription: String = "",
    @SerializedName("codeExample")      val codeExample: String = "",
    @SerializedName("category")         val category: String,
    @SerializedName("tags")             val tags: List<String> = emptyList(),
    @SerializedName("relatedTerms")     val relatedTerms: List<String> = emptyList(),
    @SerializedName("views")            val views: Int = 0,
    @SerializedName("updatedAt")        val updatedAt: String = "",
    @SerializedName("isPublished")      val isPublished: Boolean = true
) {
    fun toDomain() = Entry(
        id = id,
        title = title,
        shortDescription = shortDescription,
        fullDescription = fullDescription,
        codeExample = codeExample,
        category = category,
        tags = tags,
        relatedTerms = relatedTerms,
        views = views,
        updatedAt = updatedAt,
        isPublished = isPublished
    )
}

data class CreateEntryRequest(
    @SerializedName("title")            val title: String,
    @SerializedName("shortDescription") val shortDescription: String,
    @SerializedName("fullDescription")  val fullDescription: String,
    @SerializedName("codeExample")      val codeExample: String,
    @SerializedName("category")         val category: String,
    @SerializedName("tags")             val tags: List<String>,
    @SerializedName("relatedTerms")     val relatedTerms: List<String>,
    @SerializedName("isPublished")      val isPublished: Boolean
)
