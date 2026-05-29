package com.example.myapplication.data.remote.api

import com.example.myapplication.data.remote.dto.CreateEntryRequest
import com.example.myapplication.data.remote.dto.EntryDto
import retrofit2.http.*

interface DictionaryApi {

    @GET("entries")
    suspend fun getAllEntries(): List<EntryDto>

    @GET("entries/{id}")
    suspend fun getEntryById(@Path("id") id: String): EntryDto

    @GET("entries/category/{category}")
    suspend fun getEntriesByCategory(@Path("category") category: String): List<EntryDto>

    @GET("entries/search")
    suspend fun searchEntries(@Query("q") query: String): List<EntryDto>

    @POST("entries")
    suspend fun createEntry(@Body request: CreateEntryRequest): EntryDto

    @PUT("entries/{id}")
    suspend fun updateEntry(
        @Path("id") id: String,
        @Body request: CreateEntryRequest
    ): EntryDto

    @DELETE("entries/{id}")
    suspend fun deleteEntry(@Path("id") id: String)

    @GET("admin/stats")
    suspend fun getAdminStats(): Map<String, Int>
}
