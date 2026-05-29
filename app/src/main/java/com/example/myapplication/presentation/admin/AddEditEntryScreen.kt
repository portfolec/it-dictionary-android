package com.example.myapplication.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.presentation.components.AppTextField
import com.example.myapplication.presentation.components.PrimaryButton
import com.example.myapplication.ui.theme.*

private val availableCategories = listOf(
    "Языки", "Фреймворки", "Базы данных", "DevOps", "Алгоритмы", "Паттерны", "Сети", "ИБ"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditEntryScreen(
    entryId: String?,
    onBackClick: () -> Unit,
    onSaved: () -> Unit,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val editState by viewModel.editState.collectAsState()

    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("DevOps") }
    var shortDesc by remember { mutableStateOf("") }
    var fullDesc by remember { mutableStateOf("") }
    var codeExample by remember { mutableStateOf("") }
    var tagsInput by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf(listOf<String>()) }
    var relatedInput by remember { mutableStateOf("") }
    var related by remember { mutableStateOf(listOf<String>()) }
    var isPublished by remember { mutableStateOf(true) }
    var showCategoryDropdown by remember { mutableStateOf(false) }

    // Load existing entry data
    LaunchedEffect(entryId) {
        viewModel.loadEntry(entryId)
    }
    LaunchedEffect(editState.entry) {
        editState.entry?.let { entry ->
            title = entry.title
            selectedCategory = entry.category
            shortDesc = entry.shortDescription
            fullDesc = entry.fullDescription
            codeExample = entry.codeExample
            tags = entry.tags
            related = entry.relatedTerms
            isPublished = entry.isPublished
        }
    }
    LaunchedEffect(editState.isSaved) {
        if (editState.isSaved) onSaved()
    }

    Column(modifier = Modifier.fillMaxSize().background(White)) {
        // Top bar
        Surface(color = White, shadowElevation = 1.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Close, contentDescription = "Закрыть", tint = TextPrimary)
                    }
                    Text(
                        text = if (entryId == null) "Новый термин" else "Редактировать",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
                TextButton(onClick = {
                    viewModel.save(
                        id = entryId,
                        title = title,
                        category = selectedCategory,
                        shortDesc = shortDesc,
                        fullDesc = fullDesc,
                        code = codeExample,
                        tags = tags,
                        related = related,
                        isPublished = true
                    )
                }) {
                    Text("Сохранить", color = ElectricBlue, fontWeight = FontWeight.Medium)
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Title
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FormLabel("Название *")
                AppTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "Название термина"
                )
            }

            // Category dropdown
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FormLabel("Категория")
                Box {
                    OutlinedButton(
                        onClick = { showCategoryDropdown = true },
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = GrayBackground,
                            contentColor = TextPrimary
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(selectedCategory, fontSize = 15.sp, color = TextPrimary)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextSecondary)
                        }
                    }
                    DropdownMenu(
                        expanded = showCategoryDropdown,
                        onDismissRequest = { showCategoryDropdown = false }
                    ) {
                        availableCategories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    selectedCategory = cat
                                    showCategoryDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            // Short description
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FormLabel("Краткое описание")
                AppTextField(
                    value = shortDesc,
                    onValueChange = { shortDesc = it },
                    placeholder = "Краткое описание для карточки",
                    maxLines = 3,
                    singleLine = false
                )
            }

            // Full description
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FormLabel("Полное описание")
                AppTextField(
                    value = fullDesc,
                    onValueChange = { fullDesc = it },
                    placeholder = "Детальное описание термина",
                    maxLines = 8,
                    singleLine = false
                )
            }

            // Code example
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FormLabel("Пример кода")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkCodeBg)
                        .padding(4.dp)
                ) {
                    OutlinedTextField(
                        value = codeExample,
                        onValueChange = { codeExample = it },
                        placeholder = { Text("// Введите пример кода...", color = Color(0xFF64748B), fontFamily = FontFamily.Monospace) },
                        textStyle = LocalTextStyle.current.copy(
                            color = Color(0xFFE2E8F0),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        maxLines = 12,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Tags
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FormLabel("Теги")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppTextField(
                        value = tagsInput,
                        onValueChange = { tagsInput = it },
                        placeholder = "Добавить тег",
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            if (tagsInput.isNotBlank()) {
                                tags = tags + tagsInput.trim().lowercase()
                                tagsInput = ""
                            }
                        },
                        modifier = Modifier
                            .background(ElectricBlue, RoundedCornerShape(12.dp))
                            .size(52.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Добавить", tint = White)
                    }
                }
                if (tags.isNotEmpty()) {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        tags.forEach { tag ->
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(GrayBackground)
                                    .border(1.dp, DividerColor, RoundedCornerShape(50.dp))
                                    .padding(start = 12.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("#$tag", fontSize = 12.sp, color = TextPrimary)
                                Spacer(Modifier.width(4.dp))
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Удалить",
                                    tint = TextSecondary,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { tags = tags - tag }
                                )
                            }
                        }
                    }
                }
            }

            // Publish/Draft buttons
            PrimaryButton(
                text = "Опубликовать",
                onClick = {
                    viewModel.save(entryId, title, selectedCategory, shortDesc, fullDesc, codeExample, tags, related, true)
                },
                isLoading = editState.isLoading
            )

            OutlinedButton(
                onClick = {
                    viewModel.save(entryId, title, selectedCategory, shortDesc, fullDesc, codeExample, tags, related, false)
                },
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, TextSecondary),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Сохранить черновик", fontSize = 16.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
private fun FormLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = TextPrimary
    )
}
