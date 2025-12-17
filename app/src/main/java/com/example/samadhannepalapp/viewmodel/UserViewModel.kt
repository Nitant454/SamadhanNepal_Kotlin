package com.example.samadhannepalapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.samadhannepalapp.model.UserModel
import com.example.samadhannepalapp.repository.UserRepository
import com.example.samadhannepalapp.repository.UserRepositoryImpl

class UserViewModel(private val repository: UserRepository = UserRepositoryImpl()) : ViewModel(){

    private val _users = MutableLiveData<UserModel?>()
    val users: MutableLiveData<UserModel?> get() = _users

    // LiveData for all users
    private val _allUsers = MutableLiveData<List<UserModel>?>()
    val allUsers: MutableLiveData<List<UserModel>?> get() = _allUsers

    // ------------------- METHODS -------------------

    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        repository.login(email, password, callback)
    }

    fun register(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
        repository.register(email, password, callback)
    }

    fun addUserToDatabase(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        repository.addUserToDatabase(userId, model, callback)
    }

    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        repository.forgetPassword(email, callback)
    }

    fun deleteAccount(userId: String, callback: (Boolean, String) -> Unit) {
        repository.deleteAccount(userId, callback)
    }

    fun editProfile(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        repository.editProfile(userId, model, callback)
    }

    fun getUSerById(userId: String) {
        repository.getUSerById(userId) { success, _, data ->
            if (success) {
                _users.postValue(data)
            }
        }
    }

    fun getAllUser() {
        repository.getAllUser { success, _, data ->
            if (success) {
                _allUsers.postValue(data)
            }
        }
    }
}
