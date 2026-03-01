package com.example.samadhannepalapp.repository

import com.example.samadhannepalapp.model.UserIssueModel
import com.google.firebase.database.*

class UserIssueRepositoryImpl : UserIssueRepository {

    private val ref: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("UserIssues")

    override fun addIssue(
        issueId: String,
        model: UserIssueModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(issueId).setValue(model)
            .addOnSuccessListener {
                callback(true, "Issue added successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to add issue")
            }
    }

    override fun updateIssue(
        issueId: String,
        model: UserIssueModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(issueId).updateChildren(model.toMap())
            .addOnSuccessListener {
                callback(true, "Issue updated successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to update issue")
            }
    }

    override fun deleteIssue(
        issueId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(issueId).removeValue()
            .addOnSuccessListener {
                callback(true, "Issue deleted successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to delete issue")
            }
    }

    override fun getIssueById(
        issueId: String,
        callback: (Boolean, String, UserIssueModel?) -> Unit
    ) {
        ref.child(issueId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val issue = snapshot.getValue(UserIssueModel::class.java)

                    if (issue != null) {
                        // FIX: attach id
                        val issueWithId = issue.copy(id = snapshot.key ?: "")
                        callback(true, "Issue fetched successfully", issueWithId)
                    } else {
                        callback(false, "Issue not found", null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

    override fun getAllIssues(
        callback: (Boolean, String, List<UserIssueModel>?) -> Unit
    ) {
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val issueList = mutableListOf<UserIssueModel>()

                for (child in snapshot.children) {
                    val issue = child.getValue(UserIssueModel::class.java)

                    if (issue != null) {
                        //  FIX: attach Firebase key as id
                        val issueWithId = issue.copy(id = child.key ?: "")
                        issueList.add(issueWithId)
                    }
                }

                callback(true, "Issues fetched successfully", issueList)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun getIssuesByUserId(
        userId: String,
        callback: (Boolean, String, List<UserIssueModel>?) -> Unit
    ) {
        ref.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val issueList = mutableListOf<UserIssueModel>()

                    for (child in snapshot.children) {
                        val issue = child.getValue(UserIssueModel::class.java)

                        if (issue != null) {
                            // FIX: attach Firebase key as id
                            val issueWithId = issue.copy(id = child.key ?: "")
                            issueList.add(issueWithId)
                        }
                    }

                    callback(true, "User issues fetched successfully", issueList)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

    fun updateIssueStatus(issueId: String, newStatus: String) {
        if (issueId.isNotEmpty()) {   //  safety check
            ref.child(issueId).child("status").setValue(newStatus)
        }
    }
}