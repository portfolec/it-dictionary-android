package com.example.myapplication.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.domain.model.Entry
import com.example.myapplication.presentation.components.CategoryBadge
import com.example.myapplication.ui.theme.*

@Composable
fun AdminPanelScreen(
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (String) -> Unit,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val state by viewModel.panelState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(GrayBackground)) {
        // Header
        Surface(color = White, shadowElevation = 1.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = TextPrimary)
                }
                Text(
                    text = "Панель администратора",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats grid
            item {
                Text("Статистика", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatMetricCard("Терминов", state.stats.totalEntries.toString(), Icons.Default.MenuBook, ElectricBlue, Modifier.weight(1f))
                    StatMetricCard("Категорий", state.stats.totalCategories.toString(), Icons.Default.Category, CategoryLanguages, Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatMetricCard("Пользователей", state.stats.totalUsers.toString(), Icons.Default.People, CategoryDatabases, Modifier.weight(1f))
                    StatMetricCard("Просмотров сегодня", state.stats.todayViews.toString(), Icons.Default.Visibility, CategoryFrameworks, Modifier.weight(1f))
                }
            }

            // Section header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Последние записи", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    TextButton(onClick = onAddClick) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Добавить", color = ElectricBlue, fontSize = 14.sp)
                    }
                }
            }

            items(state.entries.take(20)) { entry ->
                AdminEntryRow(
                    entry = entry,
                    onEdit = { onEditClick(entry.id) },
                    onDelete = { viewModel.delete(entry.id) }
                )
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun StatMetricCard(
    label: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(8.dp))
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = label, fontSize = 12.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun AdminEntryRow(
    entry: Entry,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = entry.title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CategoryBadge(name = entry.category, color = getCategoryColor(entry.category))
                    // Status badge
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (entry.isPublished) Success.copy(0.1f) else TextSecondary.copy(0.1f),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (entry.isPublished) "Опубликовано" else "Черновик",
                            fontSize = 11.sp,
                            color = if (entry.isPublished) Success else TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Редактировать", tint = ElectricBlue, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Error, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
