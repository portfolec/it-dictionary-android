package com.example.myapplication.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.presentation.components.CategoryBadge
import com.example.myapplication.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EntryDetailScreen(
    entryId: String,
    onBackClick: () -> Unit,
    onRelatedTermClick: (String) -> Unit,
    viewModel: EntryDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(entryId) {
        viewModel.loadEntry(entryId)
    }

    Box(modifier = Modifier.fillMaxSize().background(White)) {
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = ElectricBlue)
                }
            }
            state.entry == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Термин не найден", color = TextSecondary)
                }
            }
            else -> {
                val entry = state.entry!!
                val categoryColor = getCategoryColor(entry.category)

                Column(modifier = Modifier.fillMaxSize()) {
                    // Sticky top nav
                    Surface(color = White, shadowElevation = 1.dp) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = onBackClick) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = TextPrimary)
                            }
                            Row {
                                IconButton(onClick = { viewModel.toggleBookmark(entry.id) }) {
                                    Icon(
                                        imageVector = if (entry.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Закладка",
                                        tint = if (entry.isBookmarked) ElectricBlue else TextSecondary
                                    )
                                }
                                IconButton(onClick = {}) {
                                    Icon(Icons.Default.Share, contentDescription = "Поделиться", tint = TextSecondary)
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        // Category badge
                        CategoryBadge(name = entry.category, color = categoryColor)

                        Spacer(Modifier.height(12.dp))

                        // Title
                        Text(
                            text = entry.title,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )

                        Spacer(Modifier.height(12.dp))

                        // Metadata
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Visibility, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(text = "${entry.views} просмотров", fontSize = 13.sp, color = TextSecondary)
                            Spacer(Modifier.width(16.dp))
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(text = "Обновлено ${entry.updatedAt}", fontSize = 13.sp, color = TextSecondary)
                        }

                        Spacer(Modifier.height(16.dp))
                        HorizontalDivider(color = DividerColor)
                        Spacer(Modifier.height(16.dp))

                        // Definition
                        Text(text = "Определение", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Spacer(Modifier.height(8.dp))
                        Text(text = entry.fullDescription, fontSize = 15.sp, color = TextPrimary, lineHeight = 24.sp)

                        if (entry.codeExample.isNotBlank()) {
                            Spacer(Modifier.height(20.dp))
                            Text(text = "Пример использования", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Spacer(Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DarkCodeBg)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = entry.codeExample,
                                    fontSize = 13.sp,
                                    color = Color(0xFFE2E8F0),
                                    fontFamily = FontFamily.Monospace,
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        if (entry.relatedTerms.isNotEmpty()) {
                            Spacer(Modifier.height(20.dp))
                            Text(text = "Связанные термины", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Spacer(Modifier.height(8.dp))
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                entry.relatedTerms.forEach { term ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50.dp))
                                            .background(GrayBackground)
                                            .border(1.dp, ElectricBlue.copy(alpha = 0.3f), RoundedCornerShape(50.dp))
                                            .clickable { onRelatedTermClick(term) }
                                            .padding(horizontal = 14.dp, vertical = 8.dp)
                                    ) {
                                        Text(text = term, fontSize = 13.sp, color = ElectricBlue)
                                    }
                                }
                            }
                        }

                        if (entry.tags.isNotEmpty()) {
                            Spacer(Modifier.height(20.dp))
                            Text(text = "Теги", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Spacer(Modifier.height(8.dp))
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                entry.tags.forEach { tag ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50.dp))
                                            .background(GrayBackground)
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(text = "#$tag", fontSize = 12.sp, color = TextSecondary)
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(80.dp))
                    }

                    // Sticky bottom
                    Surface(
                        color = White,
                        shadowElevation = 8.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { viewModel.toggleBookmark(entry.id) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (entry.isBookmarked) GrayBackground else ElectricBlue,
                                    contentColor = if (entry.isBookmarked) ElectricBlue else White
                                ),
                                modifier = Modifier.weight(1f).height(48.dp)
                            ) {
                                Text(
                                    text = if (entry.isBookmarked) "Удалить из избранного" else "В избранное",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            OutlinedButton(
                                onClick = {},
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, ElectricBlue),
                                modifier = Modifier.size(48.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
