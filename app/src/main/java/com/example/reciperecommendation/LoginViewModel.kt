package com.example.reciperecommendation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    val username = MutableStateFlow("")
    val password = MutableStateFlow("")

    val validationMessage = MutableStateFlow<String?>(null)

    fun onUsernameChange(newUsername: String) {
        username.value = newUsername
        validationMessage.value = null
    }

    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
        validationMessage.value = null
    }

    private fun isEmailValid(email: String): Boolean {
        val trimmed = email.trim()
        val at = trimmed.indexOf("@")
        return at > 0 && trimmed.indexOf(".", at) > at + 1
    }

    private fun isPhoneValid(phone: String): Boolean {
        val digits = phone.filter { it.isDigit() }
        return digits.length == 10
    }

    private fun isPasswordValid(pwd: String): Boolean {
        return pwd.length >= 6
    }

    private fun buildRepository(context: Context): UserRepository {
        val db = AppDatabase.getDatabase(context)
        return UserRepository(db.userDao())
    }

    /**
     * Existing behavior (unchanged):
     * - Validate input
     * - If user exists and password matches -> success
     * - If user exists but password mismatch -> failure
     * - If user does not exist -> create user (implicit signup) and login -> success
     */
    fun loginUser(
        context: Context,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val uname = username.value.trim()
        val pwd = password.value

        if (uname.isEmpty() || pwd.isEmpty()) {
            onFailure(); return
        }
        if (!(isEmailValid(uname) || isPhoneValid(uname))) {
            onFailure(); return
        }
        if (!isPasswordValid(pwd)) {
            onFailure(); return
        }

        viewModelScope.launch {
            val repo = buildRepository(context)
            val user = withContext(Dispatchers.IO) { repo.findUser(uname) }
            if (user != null) {
                if (user.password == pwd) {
                    withContext(Dispatchers.IO) { repo.setLoggedIn(user.id) }
                    onSuccess()
                } else {
                    onFailure()
                }
            } else {
                // implicit signup
                val res = withContext(Dispatchers.IO) { repo.createUser(uname, pwd) }
                if (res.isSuccess) {
                    val id = res.getOrNull() ?: -1L
                    if (id > 0) {
                        withContext(Dispatchers.IO) { repo.setLoggedIn(id) }
                        onSuccess()
                    } else {
                        onFailure()
                    }
                } else {
                    onFailure()
                }
            }
        }
    }

    /**
     * Explicit signup (for the SignupScreen). This will:
     * - Validate inputs (email/phone + pwd + confirm match)
     * - Create new user if username not taken
     * - Mark user as logged in and call onSuccess
     */
    fun signupUser(
        context: Context,
        unameRaw: String,
        pwd: String,
        confirmPwd: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val uname = unameRaw.trim()

        if (uname.isEmpty()) {
            onFailure("Enter email or phone")
            return
        }
        if (!(isEmailValid(uname) || isPhoneValid(uname))) {
            onFailure("Enter a valid email (contains @) or a 10-digit phone number")
            return
        }
        if (!isPasswordValid(pwd)) {
            onFailure("Password must be at least 6 characters")
            return
        }
        if (pwd != confirmPwd) {
            onFailure("Passwords do not match")
            return
        }

        viewModelScope.launch {
            val repo = buildRepository(context)
            val existing = withContext(Dispatchers.IO) { repo.findUser(uname) }
            if (existing != null) {
                onFailure("User already exists. Please login.")
                return@launch
            }

            val res = withContext(Dispatchers.IO) { repo.createUser(uname, pwd) }
            if (res.isSuccess) {
                val id = res.getOrNull() ?: -1L
                if (id > 0) {
                    withContext(Dispatchers.IO) { repo.setLoggedIn(id) }
                    onSuccess()
                } else {
                    onFailure("Failed to create user")
                }
            } else {
                onFailure("Failed to create user: ${res.exceptionOrNull()?.localizedMessage ?: "unknown"}")
            }
        }
    }
}
