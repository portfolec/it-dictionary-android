package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.model.UserRole
import com.example.myapplication.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        // Демо-режим: admin@example.com / любой пароль = Администратор
        if (email == "admin@example.com") {
            return Result.success(
                User(
                    id = "demo-admin-001",
                    name = "Администратор",
                    email = email,
                    role = UserRole.ADMIN
                )
            )
        }
        // Демо-режим: любой другой email = Пользователь
        if (email.isNotBlank() && password.isNotBlank() && !email.contains("@").not()) {
            return try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = result.user
                    ?: return demoUserLogin(email)
                val isAdmin = email.contains("admin")
                Result.success(
                    User(
                        id = firebaseUser.uid,
                        name = firebaseUser.displayName ?: email.substringBefore("@"),
                        email = firebaseUser.email ?: email,
                        role = if (isAdmin) UserRole.ADMIN else UserRole.USER
                    )
                )
            } catch (e: Exception) {
                // Firebase недоступна — используем демо-режим
                demoUserLogin(email)
            }
        }
        return Result.failure(Exception("Введите email и пароль"))
    }

    private fun demoUserLogin(email: String): Result<User> {
        val isAdmin = email.contains("admin")
        return Result.success(
            User(
                id = "demo-${email.hashCode()}",
                name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                email = email,
                role = if (isAdmin) UserRole.ADMIN else UserRole.USER
            )
        )
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        isAdmin: Boolean
    ): Result<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Ошибка регистрации"))
            Result.success(
                User(
                    id = firebaseUser.uid,
                    name = name,
                    email = email,
                    role = if (isAdmin) UserRole.ADMIN else UserRole.USER
                )
            )
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка регистрации: ${e.message}"))
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser ?: return null
        val isAdmin = firebaseUser.email?.contains("admin") == true
        return User(
            id = firebaseUser.uid,
            name = firebaseUser.displayName ?: firebaseUser.email?.substringBefore("@") ?: "Пользователь",
            email = firebaseUser.email ?: "",
            role = if (isAdmin) UserRole.ADMIN else UserRole.USER
        )
    }

    override fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null
}
