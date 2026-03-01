package com.example.samadhannepalapp.repository

import com.example.samadhannepalapp.model.UserIssueModel

interface UserIssueRepository {
    fun addIssue(
        issueId: String,
        model: UserIssueModel,
        callback: (Boolean, String) -> Unit
    )

    fun updateIssue(
        issueId: String,
        model: UserIssueModel,
        callback: (Boolean, String) -> Unit
    )

    fun deleteIssue(
        issueId: String,
        callback: (Boolean, String) -> Unit
    )

    fun getIssueById(
        issueId: String,
        callback: (Boolean, String, UserIssueModel?) -> Unit
    )

    fun getAllIssues(
        callback: (Boolean, String, List<UserIssueModel>?) -> Unit
    )
    fun getIssuesByUserId(
        userId: String,
        callback: (Boolean, String, List<UserIssueModel>?) -> Unit
    )
}