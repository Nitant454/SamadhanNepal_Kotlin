package com.example.samadhannepalapp.repository

import com.example.samadhannepalapp.model.UserModel

interface UserRepository {

    fun login(
        email: String, password: String,
        callback: (Boolean, String) -> Unit)

    fun register(
        email: String,password: String, role: String,
        callback: (Boolean, String) -> Unit)

    fun addUserToDatabase(
        userId: String, model: UserModel,
        callback: (Boolean, String) -> Unit)

    fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit)

    fun deleteAccount(
        userId: String,
        callback: (Boolean, String) -> Unit)

    fun editProfile(
        userId:String,model: UserModel,
        callback: (Boolean, String) -> Unit)

    fun getUSerById(
        userId: String,
        callback: (Boolean, String, UserModel?) -> Unit)

    fun getAllUser(
        callback: (Boolean, String, List<UserModel>?) -> Unit)

    fun getUserRole(
        userId: String,
        callback: (Boolean, String?) -> Unit)
}
