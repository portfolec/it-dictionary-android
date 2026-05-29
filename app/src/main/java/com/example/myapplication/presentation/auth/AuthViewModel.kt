package com.example.myapplication.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.usecase.GetCurrentUserUseCase
import com.example.myapplication.domain.usecase.LoginUseCase
import com.example.myapplication.domain.usecase.LogoutUseCase
import com.example.myapplication.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            _state.value = _state.value.copy(user = user)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = loginUseCase(email, password)
            result.fold(
                onSuccess = { user ->
                    _state.value = _state.value.copy(isLoading = false, user = user)
                },
                onFailure = { e ->
                    _state.value = _state.value.copy(isLoading = false, error = e.message)
                }
            )
        }
    }

    fun register(name: String, email: String, password: String, isAdmin: Boolean) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = registerUseCase(name, email, password, isAdmin)
            result.fold(
                onSuccess = { user ->
                    _state.value = _state.value.copy(isLoading = false, user = user)
                },
                onFailure = { e ->
                    _state.value = _state.value.copy(isLoading = false, error = e.message)
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _state.value = AuthState()
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
