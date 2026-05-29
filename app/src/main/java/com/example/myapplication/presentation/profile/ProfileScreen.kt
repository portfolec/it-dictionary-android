package com.example.myapplication.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.model.UserRole
import com.example.myapplication.ui.theme.*

@Composable
fun ProfileScreen(
    user: User?,
    onLogout: () -> Unit,
    onAdminPanelClick: () -> Unit
) {
    val displayName = user?.name ?: "Пользователь"
    val initials = displayName.split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifEmpty { "П" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Profile header
        Surface(color = White, shadowElevation = 1.dp) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(ElectricBlue, ElectricBlueLight)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = displayName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                Text(
                    text = user?.email ?: "",
                    fontSize = 14.sp,
                    color = TextSecondary
                )

                Spacer(Modifier.height(8.dp))

                // Role badge
                val isAdmin = user?.role == UserRole.ADMIN
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isAdmin) Error.copy(alpha = 0.1f) else ElectricBlue.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isAdmin) "Администратор" else "Пользователь",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isAdmin) Error else ElectricBlue
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Stats row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard("Просмотрено", user?.viewedCount ?: 47, modifier = Modifier.weight(1f))
            StatCard("В избранном", user?.bookmarksCount ?: 15, modifier = Modifier.weight(1f))
            StatCard("Дней в приложении", user?.daysInApp ?: 12, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        // Account
        SettingsSection(title = "АККАУНТ") {
            SettingsRow(icon = Icons.Default.Person, label = "Редактировать профиль", onClick = {})
            HorizontalDivider(color = DividerColor)
            SettingsRow(icon = Icons.Default.Lock, label = "Сменить пароль", onClick = {})
        }

        Spacer(Modifier.height(12.dp))

        // App settings
        SettingsSection(title = "ПРИЛОЖЕНИЕ") {
            SettingsRow(icon = Icons.Default.Palette, label = "Тема оформления", badge = "Светлая", onClick = {})
            HorizontalDivider(color = DividerColor)
            SettingsRow(icon = Icons.Default.Language, label = "Язык", badge = "Русский", onClick = {})
            HorizontalDivider(color = DividerColor)
            SettingsRow(icon = Icons.Default.Notifications, label = "Уведомления", onClick = {})
        }

        // Admin panel
        if (user?.role == UserRole.ADMIN) {
            Spacer(Modifier.height(12.dp))
            SettingsSection(title = "УПРАВЛЕНИЕ КОНТЕНТОМ") {
                SettingsRow(
                    icon = Icons.Default.Settings,
                    label = "Панель администратора",
                    onClick = onAdminPanelClick
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // About
        SettingsSection(title = "О ПРИЛОЖЕНИИ") {
            SettingsRow(icon = Icons.Default.Info, label = "Версия", badge = "1.0.0", onClick = {})
            HorizontalDivider(color = DividerColor)
            SettingsRow(icon = Icons.Default.PrivacyTip, label = "Политика конфиденциальности", onClick = {})
            HorizontalDivider(color = DividerColor)
            SettingsRow(icon = Icons.Default.HelpOutline, label = "Поддержка", onClick = {})
        }

        Spacer(Modifier.height(12.dp))

        // Logout
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .shadow(2.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onLogout)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, tint = Error, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(12.dp))
                Text(text = "Выйти", fontSize = 15.sp, color = Error, fontWeight = FontWeight.Medium)
            }
        }

        Spacer(Modifier.height(100.dp))
    }
}

@Composable
private fun StatCard(label: String, value: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricBlue,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                color = TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    label: String,
    badge: String? = null,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isDestructive) Error else TextSecondary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            color = if (isDestructive) Error else TextPrimary,
            modifier = Modifier.weight(1f)
        )
        if (badge != null) {
            Text(text = badge, fontSize = 14.sp, color = TextSecondary)
            Spacer(Modifier.width(4.dp))
        }
        if (!isDestructive) {
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
