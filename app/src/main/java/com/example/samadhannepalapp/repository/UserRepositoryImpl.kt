package com.example.samadhannepalapp.repository

import com.example.samadhannepalapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UserRepositoryImpl : UserRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val ref: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Users")


    override fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                callback(true, "Login successful")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Login failed")
            }
    }

    override fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid = auth.currentUser?.uid ?: ""
                callback(true, "Registration successful", uid)
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Registration failed", "")
            }
    }

    override fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                callback(true, "Password reset email sent")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to send reset email")
            }
    }

    override fun deleteAccount(
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        val user = auth.currentUser

        if (user == null || user.uid != userId) {
            callback(false, "Unauthorized action")
            return
        }

        user.delete()
            .addOnSuccessListener {
                ref.child(userId).removeValue()
                callback(true, "Account deleted successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Account deletion failed")
            }
    }

    override fun addUserToDatabase(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).setValue(model)
            .addOnSuccessListener {
                callback(true, "User added to database")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to add user")
            }
    }

    override fun editProfile(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).updateChildren(model.toMap())
            .addOnSuccessListener {
                callback(true, "Profile updated successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Profile update failed")
            }
    }

    override fun getUSerById(
        userId: String,
        callback: (Boolean, String, UserModel?) -> Unit
    ) {
        ref.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        callback(false, "User not found", null)
                        return
                    }
                    val user = snapshot.getValue(UserModel::class.java)
                    callback(true, "User fetched successfully", user)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

    override fun getAllUser(
        callback: (Boolean, String, List<UserModel>?) -> Unit
    ) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<UserModel>()
                for (child in snapshot.children) {
                    child.getValue(UserModel::class.java)?.let {
                        users.add(it)
                    }
                }
                callback(true, "Users fetched successfully", users)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }
}
