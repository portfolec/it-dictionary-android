package com.example.myapplication.presentation.bookmarks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.presentation.components.CategoryChip
import com.example.myapplication.presentation.components.EntryCard
import com.example.myapplication.presentation.components.PrimaryButton
import com.example.myapplication.ui.theme.*

@Composable
fun BookmarksScreen(
    onEntryClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: BookmarksViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val folders = listOf("Все", "Работа", "Учёба", "Личное")

    Column(modifier = Modifier.fillMaxSize().background(GrayBackground)) {
        // Header
        Surface(color = White, shadowElevation = 1.dp) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                Text(
                    text = "Избранное",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                if (state.entries.isNotEmpty()) {
                    Text(
                        text = "${state.entries.size} терминов сохранено",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        if (state.entries.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        tint = TextSecondary.copy(alpha = 0.4f),
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = "Здесь будут ваши закладки",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Сохраняйте термины для быстрого доступа",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(24.dp))
                    PrimaryButton(
                        text = "Перейти к поиску",
                        onClick = onSearchClick,
                        modifier = Modifier.width(200.dp)
                    )
                }
            }
        } else {
            // Folder chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(folders) { folder ->
                    CategoryChip(
                        name = folder,
                        color = ElectricBlue,
                        isSelected = state.selectedFolder == folder,
                        onClick = { viewModel.selectFolder(folder) }
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.entries, key = { it.id }) { entry ->
                    SwipeToDismissContainer(
                        onDismiss = { viewModel.removeBookmark(entry.id) }
                    ) {
                        EntryCard(
                            entry = entry,
                            onClick = { onEntryClick(entry.id) },
                            onBookmarkClick = { viewModel.removeBookmark(entry.id) }
                        )
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissContainer(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDismiss()
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Error.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                    .padding(end = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(text = "Удалить", color = Error, fontWeight = FontWeight.Medium)
            }
        },
        enableDismissFromStartToEnd = false
    ) {
        content()
    }
}
