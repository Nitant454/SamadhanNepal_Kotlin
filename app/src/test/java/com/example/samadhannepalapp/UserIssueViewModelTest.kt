package com.example.samadhannepalapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.samadhannepalapp.model.UserIssueModel
import com.example.samadhannepalapp.repository.UserIssueRepository
import com.example.samadhannepalapp.viewmodel.UserIssueViewModel
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

class UserIssueViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repository: UserIssueRepository = mock()
    private val viewModel = UserIssueViewModel(repository)

    @Test
    fun getAllIssues_success_updates_liveData() {

        val fakeList = listOf(
            UserIssueModel(
                id = "1",
                category = "Water",
                description = "No water supply",
                ward = "5",
                status = "Pending",
                imageUrl = null,
                timestamp = 123456789L,
                userId = "user123"
            )
        )

        doAnswer {
            val callback =
                it.getArgument<(Boolean, String, List<UserIssueModel>?) -> Unit>(0)
            callback(true, "Success", fakeList)
            null
        }.whenever(repository).getAllIssues(any())

        viewModel.getAllIssues()

        assertEquals(1, viewModel.issues.value?.size)
        assertEquals("Water", viewModel.issues.value?.get(0)?.category)
    }
}