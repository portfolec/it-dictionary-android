package com.example.myapplication.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.domain.model.User
import com.example.myapplication.presentation.components.AppTextField
import com.example.myapplication.presentation.components.PrimaryButton
import com.example.myapplication.presentation.components.SecondaryButton
import com.example.myapplication.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: (User) -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    LaunchedEffect(state.user) {
        state.user?.let {
            viewModel.clearError()
            onLoginSuccess(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 48.dp, bottom = 32.dp)
    ) {
        // Illustration
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        Brush.linearGradient(listOf(CategoryLanguages, CategoryAlgorithms)),
                        RoundedCornerShape(16.dp)
                    )
            )
            Spacer(Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        Brush.linearGradient(listOf(ElectricBlue, CategoryPatterns)),
                        RoundedCornerShape(16.dp)
                    )
            )
            Spacer(Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        Brush.linearGradient(listOf(CategoryDatabases, CategoryFrameworks)),
                        RoundedCornerShape(16.dp)
                    )
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Добро пожаловать",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Войдите в аккаунт",
            fontSize = 15.sp,
            color = TextSecondary
        )

        Spacer(Modifier.height(32.dp))

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

        state.error?.let { error ->
            Spacer(Modifier.height(8.dp))
            Text(text = error, color = Error, fontSize = 13.sp)
        }

        Spacer(Modifier.height(16.dp))

        PrimaryButton(
            text = "Войти",
            onClick = { viewModel.login(email, password) },
            isLoading = state.isLoading
        )

        Spacer(Modifier.height(12.dp))

        TextButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Забыли пароль?", color = ElectricBlue, fontSize = 14.sp)
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = DividerColor)
            Text(text = "  или  ", color = TextSecondary, fontSize = 14.sp)
            HorizontalDivider(modifier = Modifier.weight(1f), color = DividerColor)
        }

        SecondaryButton(
            text = "Войти через Google",
            onClick = { /* TODO Firebase Google Sign-In */ }
        )

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Нет аккаунта? ", color = TextSecondary, fontSize = 14.sp)
            TextButton(onClick = onNavigateToRegister, contentPadding = PaddingValues(0.dp)) {
                Text(text = "Зарегистрироваться", color = ElectricBlue, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "💡 Демо: admin@example.com (админ) или любой другой email",
            fontSize = 12.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
