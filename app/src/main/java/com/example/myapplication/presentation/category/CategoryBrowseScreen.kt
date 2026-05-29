package com.example.myapplication.presentation.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.presentation.components.EntryCard
import com.example.myapplication.ui.theme.*

@Composable
fun CategoryBrowseScreen(
    categoryName: String,
    onEntryClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val categoryColor = getCategoryColor(categoryName)

    LaunchedEffect(categoryName) {
        viewModel.loadCategory(categoryName)
    }

    Column(modifier = Modifier.fillMaxSize().background(GrayBackground)) {
        // Header
        Surface(color = White, shadowElevation = 1.dp) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.offset(x = (-8).dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = TextPrimary)
                    }
                    Spacer(Modifier.width(4.dp))
                    Column {
                        Text(
                            text = categoryName,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = categoryColor
                        )
                        Text(
                            text = "${state.entries.size} терминов",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    // Sort button
                    var showSortMenu by remember { mutableStateOf(false) }
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Сортировка", tint = TextSecondary)
                        }
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            listOf("Алфавит", "Просмотры").forEach { sort ->
                                DropdownMenuItem(
                                    text = { Text(sort) },
                                    onClick = {
                                        viewModel.setSortBy(sort)
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = categoryColor)
            }
        } else {
            val grouped = state.entries.groupBy { it.title.first().uppercaseChar() }.toSortedMap()

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                grouped.forEach { (letter, entries) ->
                    item {
                        Text(
                            text = letter.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = categoryColor,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    items(entries) { entry ->
                        EntryCard(
                            entry = entry,
                            onClick = { onEntryClick(entry.id) }
                        )
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}
