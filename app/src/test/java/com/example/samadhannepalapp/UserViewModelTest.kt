package com.example.samadhannepalapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.samadhannepalapp.repository.UserRepository
import com.example.samadhannepalapp.viewmodel.UserViewModel
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

class UserViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repository: UserRepository = mock()
    private val viewModel = UserViewModel(repository)

    @Test
    fun login_success_test() {

        doAnswer {
            val callback = it.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Login Success")
            null
        }.whenever(repository).login(eq("test@gmail.com"), eq("123456"), any())

        var result = false
        var message = ""

        viewModel.login("test@gmail.com", "123456") { success, msg ->
            result = success
            message = msg
        }

        assertTrue(result)
        assertEquals("Login Success", message)
    }

    @Test
    fun forgetPassword_updates_operationStatus() {

        doAnswer {
            val callback = it.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Password reset email sent")
            null
        }.whenever(repository).forgetPassword(eq("test@gmail.com"), any())

        viewModel.forgetPassword("test@gmail.com") { _, _ -> }

        assertEquals(
            "Password reset email sent",
            viewModel.operationStatus.value
        )
    }
}