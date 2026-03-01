package com.example.samadhannepalapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.samadhannepalapp.model.UserIssueModel
import com.example.samadhannepalapp.repository.UserIssueRepository
import com.example.samadhannepalapp.repository.UserIssueRepositoryImpl


class UserIssueViewModel(
    private val repository: UserIssueRepository = UserIssueRepositoryImpl()
) : ViewModel() {

    // LIVE DATA
    private val _issues = MutableLiveData<List<UserIssueModel>>(emptyList())
    val issues: LiveData<List<UserIssueModel>> = _issues

    private val _issue = MutableLiveData<UserIssueModel?>()
    val issue: LiveData<UserIssueModel?> = _issue

    private val _operationStatus = MutableLiveData<String>()
    val operationStatus: LiveData<String> = _operationStatus

    //  CRUD OPERATIONS

    fun addIssue(issueId: String, model: UserIssueModel) {
        repository.addIssue(issueId, model) { success, message ->
            _operationStatus.postValue(message)
        }
    }

    fun updateIssue(issueId: String, model: UserIssueModel) {
        repository.updateIssue(issueId, model) { success, message ->
            _operationStatus.postValue(message)
        }
    }

    fun deleteIssue(issueId: String) {
        repository.deleteIssue(issueId) { success, message ->
            _operationStatus.postValue(message)
        }
    }

    fun getIssueById(issueId: String) {
        repository.getIssueById(issueId) { success, _, data ->
            _issue.postValue(if (success) data else null)
            if (!success) _operationStatus.postValue("Issue not found")
        }
    }

    fun getAllIssues() {
        repository.getAllIssues { success, _, data ->
            _issues.postValue(if (success && data != null) data else emptyList())
            if (!success) _operationStatus.postValue("Failed to fetch issues")
        }
    }

    fun getIssuesByUserId(userId: String) {
        repository.getIssuesByUserId(userId) { success, _, data ->
            _issues.postValue(if (success && data != null) data else emptyList())
            if (!success) _operationStatus.postValue("Failed to fetch user issues")
        }
    }

    fun updateIssueStatus(issueId: String, status: String) {
        (repository as UserIssueRepositoryImpl)
            .updateIssueStatus(issueId, status)
    }

    fun observeUserIssuesRealtime(userId: String) {
        repository.getIssuesByUserId(userId) { success, _, data ->
            _issues.postValue(if (success && data != null) data else emptyList())
            if (!success) _operationStatus.postValue("Failed to fetch user issues")
        }
    }
}