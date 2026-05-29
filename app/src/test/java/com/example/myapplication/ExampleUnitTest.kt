package com.example.myapplication

import com.example.myapplication.domain.model.Entry
import com.example.myapplication.domain.model.UserRole
import org.junit.Test
import org.junit.Assert.*

class EntryModelTest {

    @Test
    fun entry_creation_isCorrect() {
        val entry = Entry(
            id = "1",
            title = "Docker",
            shortDescription = "Платформа контейнеризации",
            fullDescription = "Docker — платформа для разработки в контейнерах",
            codeExample = "FROM ubuntu:latest",
            category = "DevOps",
            tags = listOf("devops", "containers"),
            relatedTerms = listOf("Kubernetes"),
            views = 1000,
            updatedAt = "2026-05-29"
        )

        assertEquals("Docker", entry.title)
        assertEquals("DevOps", entry.category)
        assertEquals(1000, entry.views)
        assertFalse(entry.isBookmarked)
        assertTrue(entry.isPublished)
    }

    @Test
    fun entry_bookmark_toggle() {
        val entry = Entry(
            id = "1",
            title = "Docker",
            shortDescription = "Test",
            fullDescription = "Test",
            codeExample = "",
            category = "DevOps",
            tags = emptyList(),
            relatedTerms = emptyList(),
            views = 0,
            updatedAt = "",
            isBookmarked = false
        )
        val toggled = entry.copy(isBookmarked = !entry.isBookmarked)
        assertTrue(toggled.isBookmarked)
    }

    @Test
    fun userRole_admin_check() {
        val role = UserRole.ADMIN
        assertEquals(UserRole.ADMIN, role)
        assertNotEquals(UserRole.USER, role)
    }

    @Test
    fun entry_tags_parsing() {
        val entry = Entry(
            id = "1",
            title = "Test",
            shortDescription = "",
            fullDescription = "",
            codeExample = "",
            category = "DevOps",
            tags = listOf("docker", "kubernetes", "containers"),
            relatedTerms = emptyList(),
            views = 0,
            updatedAt = ""
        )
        assertEquals(3, entry.tags.size)
        assertTrue(entry.tags.contains("docker"))
    }

    @Test
    fun entry_search_byTitle() {
        val entries = listOf(
            Entry("1", "Docker", "Контейнеры", "", "", "DevOps", emptyList(), emptyList(), 100, ""),
            Entry("2", "Python", "Язык", "", "", "Языки", emptyList(), emptyList(), 200, ""),
            Entry("3", "PostgreSQL", "БД", "", "", "Базы данных", emptyList(), emptyList(), 150, "")
        )
        val query = "docker"
        val results = entries.filter { it.title.lowercase().contains(query.lowercase()) }
        assertEquals(1, results.size)
        assertEquals("Docker", results[0].title)
    }
}
