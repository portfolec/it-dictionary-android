package com.example.myapplication.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.domain.model.UserRole
import com.example.myapplication.presentation.components.EntryCard
import com.example.myapplication.presentation.components.SectionHeader
import com.example.myapplication.ui.theme.*

private data class CategoryItem(val name: String, val color: androidx.compose.ui.graphics.Color, val icon: ImageVector)

private val categories = listOf(
    CategoryItem("Языки", CategoryLanguages, Icons.Default.Code),
    CategoryItem("Фреймворки", CategoryFrameworks, Icons.Default.AccountTree),
    CategoryItem("Базы данных", CategoryDatabases, Icons.Default.Storage),
    CategoryItem("DevOps", CategoryDevOps, Icons.Default.Cloud),
    CategoryItem("Алгоритмы", CategoryAlgorithms, Icons.Default.Memory),
    CategoryItem("Паттерны", CategoryPatterns, Icons.Default.Pattern),
    CategoryItem("Сети", CategoryNetworks, Icons.Default.Hub),
    CategoryItem("ИБ", CategorySecurity, Icons.Default.Security),
)

@Composable
fun HomeScreen(
    userRole: UserRole,
    onEntryClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onAddClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val effectiveRole = userRole

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GrayBackground)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Surface(color = White, shadowElevation = 1.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ElectricBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "</>", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = White)
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "IT Справочник",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                    IconButton(onClick = onNotificationsClick) {
                        Icon(Icons.Default.Notifications, contentDescription = "Уведомления", tint = TextSecondary)
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(20.dp))

                // Search bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(12.dp))
                        .background(White, RoundedCornerShape(12.dp))
                        .clickable(onClick = onSearchClick)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(12.dp))
                    Text(text = "Искать термины, технологии...", fontSize = 15.sp, color = TextSecondary)
                }

                Spacer(Modifier.height(24.dp))

                // Categories
                SectionHeader(title = "Категории")
                Spacer(Modifier.height(12.dp))
            }

            // Categories horizontal scroll
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    CategoryChipItem(
                        name = cat.name,
                        color = cat.color,
                        icon = cat.icon,
                        onClick = { onCategoryClick(cat.name) }
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(24.dp))

                SectionHeader(title = "Популярное сегодня")
                Spacer(Modifier.height(12.dp))

                if (state.isLoading) {
                    repeat(3) {
                        LoadingCard()
                        Spacer(Modifier.height(12.dp))
                    }
                } else {
                    state.popularEntries.forEach { entry ->
                        EntryCard(
                            entry = entry,
                            onClick = { onEntryClick(entry.id) }
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }

                Spacer(Modifier.height(12.dp))
                SectionHeader(title = "Недавно добавлено")
                Spacer(Modifier.height(12.dp))

                state.recentEntries.forEach { entry ->
                    EntryCard(
                        entry = entry,
                        onClick = { onEntryClick(entry.id) }
                    )
                    Spacer(Modifier.height(12.dp))
                }

                Spacer(Modifier.height(80.dp))
            }
        }

        // FAB for admin
        if (userRole == UserRole.ADMIN) {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = ElectricBlue,
                contentColor = White,
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 84.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить термин")
            }
        }
    }
}

@Composable
private fun CategoryChipItem(
    name: String,
    color: androidx.compose.ui.graphics.Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(color.copy(alpha = 0.1f))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = color)
    }
}

@Composable
private fun LoadingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.size(4.dp, 60.dp).background(DividerColor, RoundedCornerShape(4.dp)))
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Box(modifier = Modifier.fillMaxWidth(0.6f).height(16.dp).background(DividerColor, RoundedCornerShape(4.dp)))
                Spacer(Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth().height(12.dp).background(DividerColor, RoundedCornerShape(4.dp)))
            }
        }
    }
}
