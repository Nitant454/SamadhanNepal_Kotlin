package com.example.samadhannepalapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.samadhannepalapp.model.UserModel
import com.example.samadhannepalapp.repository.UserRepository
import com.example.samadhannepalapp.repository.UserRepositoryImpl


class UserViewModel(
    private val repository: UserRepository = UserRepositoryImpl()
) : ViewModel() {

    private val _users = MutableLiveData<UserModel?>()
    val users: LiveData<UserModel?> get() = _users

    private val _allUsers = MutableLiveData<List<UserModel>?>()
    val allUsers: LiveData<List<UserModel>?> get() = _allUsers

    private val _operationStatus = MutableLiveData<String>()
    val operationStatus: LiveData<String> get() = _operationStatus

    fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        repository.login(email, password, callback)
    }

    fun register(
        email: String,
        password: String,
        role: String,
        callback: (Boolean, String) -> Unit
    ) {
        repository.register(email, password, role, callback)
    }

    // PASSWORD RESET (Forget Password)
    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        repository.forgetPassword(email) { success, message ->
            _operationStatus.postValue(message)
            callback(success, message)
        }
    }

    // USER CRUD
    fun addUserToDatabase(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        repository.addUserToDatabase(userId, model, callback)
    }

    fun editProfile(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        repository.editProfile(userId, model, callback)
    }

    fun deleteAccount(userId: String, callback: (Boolean, String) -> Unit) {
        repository.deleteAccount(userId, callback)
    }

    fun getUSerById(userId: String) {
        repository.getUSerById(userId) { success, _, data ->
            if (success) _users.postValue(data)
        }
    }

    fun getAllUser() {
        repository.getAllUser { success, _, data ->
            if (success) _allUsers.postValue(data)
        }
    }


    fun getUserRole(
        userId: String,
        callback: (Boolean, String?) -> Unit
    ) {
        repository.getUserRole(userId, callback)
    }
}
