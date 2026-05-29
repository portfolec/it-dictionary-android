package com.example.myapplication.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.domain.model.User
import com.example.myapplication.presentation.components.AppTextField
import com.example.myapplication.presentation.components.PrimaryButton
import com.example.myapplication.ui.theme.*

@Composable
fun RegisterScreen(
    onRegisterSuccess: (User) -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isAdmin by remember { mutableStateOf(false) }

    LaunchedEffect(state.user) {
        state.user?.let { onRegisterSuccess(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 48.dp, bottom = 32.dp)
    ) {
        // Back button
        IconButton(onClick = onNavigateToLogin, modifier = Modifier.offset(x = (-12).dp)) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = TextPrimary)
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Создать аккаунт",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Присоединяйтесь к IT Справочнику",
            fontSize = 15.sp,
            color = TextSecondary
        )

        Spacer(Modifier.height(32.dp))

        AppTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = "Имя",
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
            }
        )

        Spacer(Modifier.height(12.dp))

        AppTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(Modifier.height(12.dp))

        AppTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Пароль",
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(Modifier.height(12.dp))

        AppTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = "Повторите пароль",
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(Modifier.height(20.dp))

        // Role selector
        Text(text = "Роль", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            RoleChip(
                label = "Пользователь",
                isSelected = !isAdmin,
                onClick = { isAdmin = false },
                modifier = Modifier.weight(1f)
            )
            RoleChip(
                label = "Администратор",
                isSelected = isAdmin,
                onClick = { isAdmin = true },
                modifier = Modifier.weight(1f)
            )
        }

        state.error?.let { error ->
            Spacer(Modifier.height(8.dp))
            Text(text = error, color = Error, fontSize = 13.sp)
        }

        Spacer(Modifier.height(24.dp))

        PrimaryButton(
            text = "Зарегистрироваться",
            onClick = {
                if (password == confirmPassword) {
                    viewModel.register(name, email, password, isAdmin)
                }
            },
            isLoading = state.isLoading,
            enabled = name.isNotBlank() && email.isNotBlank() && password.isNotBlank()
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Уже есть аккаунт? ", color = TextSecondary, fontSize = 14.sp)
            TextButton(onClick = onNavigateToLogin, contentPadding = PaddingValues(0.dp)) {
                Text(text = "Войти", color = ElectricBlue, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun RoleChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) ElectricBlue else White)
            .border(
                width = 1.5.dp,
                color = if (isSelected) ElectricBlue else DividerColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) White else TextPrimary
        )
    }
}
